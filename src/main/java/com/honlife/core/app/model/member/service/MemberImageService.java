package com.honlife.core.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberImage;
import com.honlife.core.app.model.member.model.MemberImageDTO;
import com.honlife.core.app.model.member.repos.MemberImageRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.util.NotFoundException;
import com.honlife.core.infra.util.ReferencedWarning;

@Service
public class MemberImageService {

    private final MemberImageRepository memberImageRepository;
    private final MemberRepository memberRepository;

    public MemberImageService(final MemberImageRepository memberImageRepository,
        final MemberRepository memberRepository) {
        this.memberImageRepository = memberImageRepository;
        this.memberRepository = memberRepository;
    }

    public List<MemberImageDTO> findAll() {
        final List<MemberImage> memberImages = memberImageRepository.findAll(Sort.by("id"));
        return memberImages.stream()
            .map(memberImage -> mapToDTO(memberImage, new MemberImageDTO()))
            .toList();
    }

    public MemberImageDTO get(final Long id) {
        return memberImageRepository.findById(id)
            .map(memberImage -> mapToDTO(memberImage, new MemberImageDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberImageDTO memberImageDTO) {
        final MemberImage memberImage = new MemberImage();
        mapToEntity(memberImageDTO, memberImage);
        return memberImageRepository.save(memberImage).getId();
    }

    public void update(final Long id, final MemberImageDTO memberImageDTO) {
        final MemberImage memberImage = memberImageRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(memberImageDTO, memberImage);
        memberImageRepository.save(memberImage);
    }

    public void delete(final Long id) {
        memberImageRepository.deleteById(id);
    }

    private MemberImageDTO mapToDTO(final MemberImage memberImage,
        final MemberImageDTO memberImageDTO) {
        memberImageDTO.setCreatedAt(memberImage.getCreatedAt());
        memberImageDTO.setUpdatedAt(memberImage.getUpdatedAt());
        memberImageDTO.setIsActive(memberImage.getIsActive());
        memberImageDTO.setId(memberImage.getId());
        memberImageDTO.setSavePath(memberImage.getSavePath());
        memberImageDTO.setType(memberImage.getType());
        memberImageDTO.setOriginName(memberImage.getOriginName());
        memberImageDTO.setRenamedName(memberImage.getRenamedName());
        memberImageDTO.setMember(memberImage.getMember() == null ? null : memberImage.getMember().getId());
        return memberImageDTO;
    }

    private MemberImage mapToEntity(final MemberImageDTO memberImageDTO,
        final MemberImage memberImage) {
        memberImage.setCreatedAt(memberImageDTO.getCreatedAt());
        memberImage.setUpdatedAt(memberImageDTO.getUpdatedAt());
        memberImage.setIsActive(memberImageDTO.getIsActive());
        memberImage.setSavePath(memberImageDTO.getSavePath());
        memberImage.setType(memberImageDTO.getType());
        memberImage.setOriginName(memberImageDTO.getOriginName());
        memberImage.setRenamedName(memberImageDTO.getRenamedName());
        final Member member = memberImageDTO.getMember() == null ? null : memberRepository.findById(memberImageDTO.getMember())
            .orElseThrow(() -> new NotFoundException("member not found"));
        memberImage.setMember(member);
        return memberImage;
    }

    public boolean memberExists(final Long id) {
        return memberImageRepository.existsByMemberId(id);
    }

}
