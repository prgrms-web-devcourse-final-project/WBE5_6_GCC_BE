package com.honlife.core.app.model.quest.service;

import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import com.honlife.core.app.model.quest.dto.MemberWeeklyQuestDTO;
import com.honlife.core.infra.response.ResponseCode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.quest.dto.WeeklyQuestProgressDTO;
import com.honlife.core.app.model.quest.repos.WeeklyQuestProgressRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WeeklyQuestProgressService {

    private final WeeklyQuestProgressRepository weeklyQuestProgressRepository;
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

    public List<WeeklyQuestProgressDTO> findAll() {
        final List<WeeklyQuestProgress> weeklyQuestProgresses = weeklyQuestProgressRepository.findAll(Sort.by("id"));
        return weeklyQuestProgresses.stream()
                .map(weeklyQuestProgress -> mapToDTO(weeklyQuestProgress, new WeeklyQuestProgressDTO()))
                .toList();
    }

    public WeeklyQuestProgressDTO get(final Long id) {
        return weeklyQuestProgressRepository.findById(id)
                .map(weeklyQuestProgress -> mapToDTO(weeklyQuestProgress, new WeeklyQuestProgressDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_QUEST));
    }

    public Long create(final WeeklyQuestProgressDTO weeklyQuestProgressDTO) {
        final WeeklyQuestProgress weeklyQuestProgress = new WeeklyQuestProgress();
        mapToEntity(weeklyQuestProgressDTO, weeklyQuestProgress);
        return weeklyQuestProgressRepository.save(weeklyQuestProgress).getId();
    }

    public void update(final Long id, final WeeklyQuestProgressDTO weeklyQuestProgressDTO) {
        final WeeklyQuestProgress weeklyQuestProgress = weeklyQuestProgressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_QUEST));
        mapToEntity(weeklyQuestProgressDTO, weeklyQuestProgress);
        weeklyQuestProgressRepository.save(weeklyQuestProgress);
    }

    public void delete(final Long id) {
        weeklyQuestProgressRepository.deleteById(id);
    }

    private WeeklyQuestProgressDTO mapToDTO(final WeeklyQuestProgress weeklyQuestProgress,
            final WeeklyQuestProgressDTO weeklyQuestProgressDTO) {
        weeklyQuestProgressDTO.setCreatedAt(weeklyQuestProgress.getCreatedAt());
        weeklyQuestProgressDTO.setUpdatedAt(weeklyQuestProgress.getUpdatedAt());
        weeklyQuestProgressDTO.setIsActive(weeklyQuestProgress.getIsActive());
        weeklyQuestProgressDTO.setId(weeklyQuestProgress.getId());
        weeklyQuestProgressDTO.setIdDone(weeklyQuestProgress.getIsDone());
        weeklyQuestProgressDTO.setMember(
            weeklyQuestProgress.getMember() == null ? null : weeklyQuestProgress.getMember().getId());
        return weeklyQuestProgressDTO;
    }

    private WeeklyQuestProgress mapToEntity(final WeeklyQuestProgressDTO weeklyQuestProgressDTO,
            final WeeklyQuestProgress weeklyQuestProgress) {
        weeklyQuestProgress.setCreatedAt(weeklyQuestProgressDTO.getCreatedAt());
        weeklyQuestProgress.setUpdatedAt(weeklyQuestProgressDTO.getUpdatedAt());
        weeklyQuestProgress.setIsActive(weeklyQuestProgressDTO.getIsActive());
        weeklyQuestProgress.setIsDone(weeklyQuestProgressDTO.getIdDone());
        final Member member = weeklyQuestProgressDTO.getMember() == null ? null : memberRepository.findById(
                weeklyQuestProgressDTO.getMember())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        weeklyQuestProgress.setMember(member);
        return weeklyQuestProgress;
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
}
