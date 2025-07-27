package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.app.model.quest.code.QuestDomain;
import com.honlife.core.app.model.quest.code.QuestType;
import com.honlife.core.app.model.quest.domain.WeeklyQuest;
import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import com.honlife.core.app.model.quest.dto.MemberWeeklyQuestDTO;
import com.honlife.core.app.model.quest.router.QuestProgressProcessorRouter;
import com.honlife.core.infra.event.CommonEvent;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    private final WeeklyQuestService weeklyQuestService;
    private final MemberRepository memberRepository;

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

    /**
     * 사용자가 이번주의 주간퀘스트를 받았는지 확인하고, 아니라면 주간 퀘스트 부여
     * @param event 이벤트 객체
     */
    @Async
    @Transactional
    public CompletableFuture<Void> renewWeeklyQuests(CommonEvent event) {
        String userEmail = event.getMemberEmail();
        LocalDateTime now = LocalDateTime.now();

        // 이번주 월요일 00:00:00
        LocalDateTime monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .with(LocalTime.MIN);
        // 이번주 일요일 23:59:59
        LocalDateTime sunday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            .with(LocalTime.MAX);

        // 이번주 주간 퀘스트를 받지 못한 경우
        List<WeeklyQuestProgress> weeklyQuests = weeklyQuestProgressRepository.findByMemberEmailAndStartAtAndEndAt(userEmail, monday, sunday);
        if (weeklyQuests.isEmpty()) {
            // 새로운 주간 퀘스트 부여
            List<WeeklyQuest> newWeeklyQuests = weeklyQuestService.getRandomQuests(5);
            Member member = memberRepository.findByEmailIgnoreCase(userEmail);
            List<WeeklyQuestProgress> progresses = new ArrayList<>();
            for(WeeklyQuest quest : newWeeklyQuests) {
                WeeklyQuestProgress progress = WeeklyQuestProgress.builder()
                    .startAt(monday)
                    .endAt(sunday)
                    .member(member)
                    .weeklyQuest(quest)
                    .build();
                progresses.add(progress);
            }
            weeklyQuestProgressRepository.saveAll(progresses);
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * 모든 활성화되어있던 주간퀘스트를 비활성화 처리
     */
    @Transactional
    public void deactivatePreviousWeeklyQuests() {
        weeklyQuestProgressRepository.deactivateAllActiveWeeklyQuests();
    }
}
