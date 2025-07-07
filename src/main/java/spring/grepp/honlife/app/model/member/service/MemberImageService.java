package spring.grepp.honlife.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberImage;
import spring.grepp.honlife.app.model.member.model.MemberImageDTO;
import spring.grepp.honlife.app.model.member.repos.MemberImageRepository;
import spring.grepp.honlife.app.model.member.repos.MemberRepository;
import spring.grepp.honlife.infra.util.NotFoundException;
import spring.grepp.honlife.infra.util.ReferencedWarning;

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
        return memberImage;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final MemberImage memberImage = memberImageRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Member memberImageMember = memberRepository.findFirstByMemberImage(memberImage);
        if (memberImageMember != null) {
            referencedWarning.setKey("memberImage.member.memberImage.referenced");
            referencedWarning.addParam(memberImageMember.getId());
            return referencedWarning;
        }
        return null;
    }

}
