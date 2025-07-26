package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.member.service.MemberPointService;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.app.model.quest.domain.EventQuestProgress;
import com.honlife.core.app.model.quest.dto.MemberEventQuestDTO;
import com.honlife.core.app.model.quest.repos.EventQuestProgressRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventQuestProgressService {

    private final EventQuestProgressRepository eventQuestProgressRepository;
    private final MemberPointService memberPointService;

    /**
     * 회원에게 할당되었고, 활성화 상태인 이벤트 퀘스트 목록 검색
     * @param userEmail 회원 이메일
     * @return List of {@link MemberEventQuestDTO}
     */
    @Transactional(readOnly = true)
    public List<MemberEventQuestDTO> getMemberEventQuestsProgress(String userEmail) {
        List<EventQuestProgress> memberEventQuestProgressList = eventQuestProgressRepository.findAllByMember_EmailAndIsActive(userEmail,true);
        return memberEventQuestProgressList.stream().map(MemberEventQuestDTO::fromEntity).collect(
            Collectors.toList());
    }

    /**
     * Check the progress of Event Quest and grant point to member
     * @param userEmail
     * @param progressId
     */
    @Transactional
    public void grantReward(String userEmail, Long progressId) {
        EventQuestProgress eventQuestProgress = eventQuestProgressRepository.findByMember_EmailAndId(userEmail, progressId)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        Integer target = eventQuestProgress.getEventQuest().getTarget();
        Integer progress = eventQuestProgress.getProgress();

        // 잘못된 호출 방지
        if(progress < target) throw new CommonException(ResponseCode.BAD_REQUEST);

        // add point to member
        String questKey = eventQuestProgress.getEventQuest().getKey();
        memberPointService.addPoint(userEmail, questKey, PointSourceType.WEEKLY);

        // check as done
        eventQuestProgress.setIsDone(true);
        eventQuestProgressRepository.save(eventQuestProgress);
    }
}
