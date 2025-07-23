package com.honlife.core.app.model.member.service;

import com.honlife.core.app.model.member.domain.MemberWeeklyQuest;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.model.MemberWeeklyQuestDTO;
import com.honlife.core.app.model.member.repos.MemberQuestRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final MemberRepository memberRepository;

    public MemberQuestService(final MemberQuestRepository memberQuestRepository,
            final MemberRepository memberRepository) {
        this.memberQuestRepository = memberQuestRepository;
        this.memberRepository = memberRepository;
    }

    public List<MemberWeeklyQuestDTO> findAll() {
        final List<MemberWeeklyQuest> memberWeeklyQuests = memberQuestRepository.findAll(Sort.by("id"));
        return memberWeeklyQuests.stream()
                .map(memberWeeklyQuest -> mapToDTO(memberWeeklyQuest, new MemberWeeklyQuestDTO()))
                .toList();
    }

    public MemberWeeklyQuestDTO get(final Long id) {
        return memberQuestRepository.findById(id)
                .map(memberWeeklyQuest -> mapToDTO(memberWeeklyQuest, new MemberWeeklyQuestDTO()))
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_QUEST));
    }

    public Long create(final MemberWeeklyQuestDTO memberWeeklyQuestDTO) {
        final MemberWeeklyQuest memberWeeklyQuest = new MemberWeeklyQuest();
        mapToEntity(memberWeeklyQuestDTO, memberWeeklyQuest);
        return memberQuestRepository.save(memberWeeklyQuest).getId();
    }

    public void update(final Long id, final MemberWeeklyQuestDTO memberWeeklyQuestDTO) {
        final MemberWeeklyQuest memberWeeklyQuest = memberQuestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_QUEST));
        mapToEntity(memberWeeklyQuestDTO, memberWeeklyQuest);
        memberQuestRepository.save(memberWeeklyQuest);
    }

    public void delete(final Long id) {
        memberQuestRepository.deleteById(id);
    }

    private MemberWeeklyQuestDTO mapToDTO(final MemberWeeklyQuest memberWeeklyQuest,
            final MemberWeeklyQuestDTO memberWeeklyQuestDTO) {
        memberWeeklyQuestDTO.setCreatedAt(memberWeeklyQuest.getCreatedAt());
        memberWeeklyQuestDTO.setUpdatedAt(memberWeeklyQuest.getUpdatedAt());
        memberWeeklyQuestDTO.setIsActive(memberWeeklyQuest.getIsActive());
        memberWeeklyQuestDTO.setId(memberWeeklyQuest.getId());
        memberWeeklyQuestDTO.setReferenceKey(memberWeeklyQuest.getReferenceKey());
        memberWeeklyQuestDTO.setIdDone(memberWeeklyQuest.getIsDone());
        memberWeeklyQuestDTO.setMember(
            memberWeeklyQuest.getMember() == null ? null : memberWeeklyQuest.getMember().getId());
        return memberWeeklyQuestDTO;
    }

    private MemberWeeklyQuest mapToEntity(final MemberWeeklyQuestDTO memberWeeklyQuestDTO,
            final MemberWeeklyQuest memberWeeklyQuest) {
        memberWeeklyQuest.setCreatedAt(memberWeeklyQuestDTO.getCreatedAt());
        memberWeeklyQuest.setUpdatedAt(memberWeeklyQuestDTO.getUpdatedAt());
        memberWeeklyQuest.setIsActive(memberWeeklyQuestDTO.getIsActive());
        memberWeeklyQuest.setReferenceKey(memberWeeklyQuestDTO.getReferenceKey());
        memberWeeklyQuest.setIsDone(memberWeeklyQuestDTO.getIdDone());
        final Member member = memberWeeklyQuestDTO.getMember() == null ? null : memberRepository.findById(
                memberWeeklyQuestDTO.getMember())
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        memberWeeklyQuest.setMember(member);
        return memberWeeklyQuest;
    }

    /**
     * 멤버 아이디를 통해 조회하여 연관된 모든 멤버퀘스트를 삭제합니다.
     * @param memberId 멤버 식별아이디
     */
    @Transactional
    public void softDropMemberQuestByMemberId(Long memberId) {
        memberQuestRepository.softDropByMemberId(memberId);
    }

    /**
     * 해당 멤버와 연관된 활성화된 첫번째 멤버 퀘스트를 조회합니다.
     * @param member 멤버
     * @param isActive 활성화 상태
     * @return {@link MemberWeeklyQuest}
     */
    public MemberWeeklyQuest findFirstMemberQuestByMemberAndIsActive(Member member, boolean isActive) {
        return memberQuestRepository.findFirstByMemberAndIsActive(member, isActive);
    }
}
