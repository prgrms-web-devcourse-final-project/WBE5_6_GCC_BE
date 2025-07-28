package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.app.model.quest.code.QuestType;
import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import com.honlife.core.app.model.quest.dto.MemberWeeklyQuestDTO;
import com.honlife.core.app.model.quest.router.QuestProgressProcessorRouter;
import com.honlife.core.infra.event.CommonEvent;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.quest.repos.WeeklyQuestProgressRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WeeklyQuestProgressService {

    private final WeeklyQuestProgressRepository weeklyQuestProgressRepository;
    private final QuestProgressProcessorRouter questProgressProcessorRouter;
    private final MemberPointService memberPointService;

    /**
     * 회원에게 할당되고, 활성화 상태인 주간 퀘스트 목록 검색
     * @param userEmail 회원 이메일
     * @return List of {@link MemberWeeklyQuestDTO}
     */
    @Transactional(readOnly = true)
    public List<MemberWeeklyQuestDTO> getMemberWeeklyQuestsProgress(String userEmail) {
        List<WeeklyQuestProgress> memberWeeklyQuestProgressList = weeklyQuestProgressRepository.findAllByMember_EmailAndIsActive(userEmail, true);
        return memberWeeklyQuestProgressList.stream().map(MemberWeeklyQuestDTO::fromEntity).collect(
            Collectors.toList());
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 멤버퀘스트를 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropMemberQuestByMemberId(Long memberId) {
        weeklyQuestProgressRepository.softDropByMemberId(memberId);
    }

    /**
     * 해당 멤버와 연관된 활성화된 첫번째 멤버 퀘스트를 조회합니다.
     * @param member 멤버
     * @param isActive 활성화 상태
     * @return {@link WeeklyQuestProgress}
     */
    public WeeklyQuestProgress findFirstMemberQuestByMemberAndIsActive(Member member, boolean isActive) {
        return weeklyQuestProgressRepository.findFirstByMemberAndIsActive(member, isActive);
    }

    /**
     * Check the progress of Weekly Quest and grant point to member
     * @param userEmail
     * @param progressId
     */
    @Transactional
    public void grantReward(String userEmail, Long progressId) {
        WeeklyQuestProgress weeklyQuestProgress = weeklyQuestProgressRepository.findByMember_EmailAndId(userEmail, progressId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        Integer target = weeklyQuestProgress.getWeeklyQuest().getTarget();
        Integer progress = weeklyQuestProgress.getProgress();

        // 잘못된 호출 방지
        if(progress < target) throw new CommonException(ResponseCode.BAD_REQUEST);

        // add point to member
        String questKey = weeklyQuestProgress.getWeeklyQuest().getKey();
        memberPointService.addPoint(userEmail, questKey, PointSourceType.WEEKLY);

        // check as done
        weeklyQuestProgress.setIsDone(true);
        weeklyQuestProgressRepository.save(weeklyQuestProgress);
    }

    @Async
    @Transactional
    public void processWeeklyQuestProgress(CommonEvent event) {
        String userEmail = event.getMemberEmail();

        // 보상수령 되지 않은 퀘스트만 검색
        List<WeeklyQuestProgress> weeklyQuests = weeklyQuestProgressRepository
            .findAllByMember_EmailAndIsActiveAndIsDone(userEmail, true, false);

        // 처리해야할 퀘스트가 없다면 return
        if (weeklyQuests.isEmpty()) return;

        for(WeeklyQuestProgress questProgress : weeklyQuests) {
            QuestType questType = questProgress.getWeeklyQuest().getType();
            Long progressId = questProgress.getId();
            questProgressProcessorRouter.routeAndProcess(QuestDomain.WEEKLY, questType, event, progressId);
        }
    }
}
