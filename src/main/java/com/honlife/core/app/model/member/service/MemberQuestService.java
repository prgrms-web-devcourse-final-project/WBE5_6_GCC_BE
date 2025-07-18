package com.honlife.core.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberQuest;
import com.honlife.core.app.model.member.model.MemberQuestDTO;
import com.honlife.core.app.model.member.repos.MemberQuestRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class MemberQuestService {

    private final MemberQuestRepository memberQuestRepository;
    private final MemberRepository memberRepository;

    public MemberQuestService(final MemberQuestRepository memberQuestRepository,
            final MemberRepository memberRepository) {
        this.memberQuestRepository = memberQuestRepository;
        this.memberRepository = memberRepository;
    }

    public List<MemberQuestDTO> findAll() {
        final List<MemberQuest> memberQuests = memberQuestRepository.findAll(Sort.by("id"));
        return memberQuests.stream()
                .map(memberQuest -> mapToDTO(memberQuest, new MemberQuestDTO()))
                .toList();
    }

    public MemberQuestDTO get(final Long id) {
        return memberQuestRepository.findById(id)
                .map(memberQuest -> mapToDTO(memberQuest, new MemberQuestDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberQuestDTO memberQuestDTO) {
        final MemberQuest memberQuest = new MemberQuest();
        mapToEntity(memberQuestDTO, memberQuest);
        return memberQuestRepository.save(memberQuest).getId();
    }

    public void update(final Long id, final MemberQuestDTO memberQuestDTO) {
        final MemberQuest memberQuest = memberQuestRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberQuestDTO, memberQuest);
        memberQuestRepository.save(memberQuest);
    }

    public void delete(final Long id) {
        memberQuestRepository.deleteById(id);
    }

    private MemberQuestDTO mapToDTO(final MemberQuest memberQuest,
            final MemberQuestDTO memberQuestDTO) {
        memberQuestDTO.setCreatedAt(memberQuest.getCreatedAt());
        memberQuestDTO.setUpdatedAt(memberQuest.getUpdatedAt());
        memberQuestDTO.setIsActive(memberQuest.getIsActive());
        memberQuestDTO.setId(memberQuest.getId());
        memberQuestDTO.setReferenceKey(memberQuest.getReferenceKey());
        memberQuestDTO.setIdDone(memberQuest.getIdDone());
        memberQuestDTO.setMember(memberQuest.getMember() == null ? null : memberQuest.getMember().getId());
        return memberQuestDTO;
    }

    private MemberQuest mapToEntity(final MemberQuestDTO memberQuestDTO,
            final MemberQuest memberQuest) {
        memberQuest.setCreatedAt(memberQuestDTO.getCreatedAt());
        memberQuest.setUpdatedAt(memberQuestDTO.getUpdatedAt());
        memberQuest.setIsActive(memberQuestDTO.getIsActive());
        memberQuest.setReferenceKey(memberQuestDTO.getReferenceKey());
        memberQuest.setIdDone(memberQuestDTO.getIdDone());
        final Member member = memberQuestDTO.getMember() == null ? null : memberRepository.findById(memberQuestDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        memberQuest.setMember(member);
        return memberQuest;
    }

}
