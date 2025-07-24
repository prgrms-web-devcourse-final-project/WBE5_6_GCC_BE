package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.quest.domain.EventQuestProgress;
import com.honlife.core.app.model.quest.dto.MemberEventQuestDTO;
import com.honlife.core.app.model.quest.repos.EventQuestProgressRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventQuestProgressService {

    private final EventQuestProgressRepository eventQuestProgressRepository;

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
}
