package spring.grepp.honlife.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberPoint;
import spring.grepp.honlife.app.model.member.model.MemberPointDTO;
import spring.grepp.honlife.app.model.member.repos.MemberPointRepository;
import spring.grepp.honlife.app.model.member.repos.MemberRepository;
import spring.grepp.honlife.infra.util.NotFoundException;
import spring.grepp.honlife.infra.util.ReferencedWarning;


@Service
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;
    private final MemberRepository memberRepository;

    public MemberPointService(final MemberPointRepository memberPointRepository,
            final MemberRepository memberRepository) {
        this.memberPointRepository = memberPointRepository;
        this.memberRepository = memberRepository;
    }

    public List<MemberPointDTO> findAll() {
        final List<MemberPoint> memberPoints = memberPointRepository.findAll(Sort.by("id"));
        return memberPoints.stream()
                .map(memberPoint -> mapToDTO(memberPoint, new MemberPointDTO()))
                .toList();
    }

    public MemberPointDTO get(final Long id) {
        return memberPointRepository.findById(id)
                .map(memberPoint -> mapToDTO(memberPoint, new MemberPointDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberPointDTO memberPointDTO) {
        final MemberPoint memberPoint = new MemberPoint();
        mapToEntity(memberPointDTO, memberPoint);
        return memberPointRepository.save(memberPoint).getId();
    }

    public void update(final Long id, final MemberPointDTO memberPointDTO) {
        final MemberPoint memberPoint = memberPointRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberPointDTO, memberPoint);
        memberPointRepository.save(memberPoint);
    }

    public void delete(final Long id) {
        memberPointRepository.deleteById(id);
    }

    private MemberPointDTO mapToDTO(final MemberPoint memberPoint,
            final MemberPointDTO memberPointDTO) {
        memberPointDTO.setCreatedAt(memberPoint.getCreatedAt());
        memberPointDTO.setUpdatedAt(memberPoint.getUpdatedAt());
        memberPointDTO.setIsActive(memberPoint.getIsActive());
        memberPointDTO.setId(memberPoint.getId());
        memberPointDTO.setPoint(memberPoint.getPoint());
        return memberPointDTO;
    }

    private MemberPoint mapToEntity(final MemberPointDTO memberPointDTO,
            final MemberPoint memberPoint) {
        memberPoint.setCreatedAt(memberPointDTO.getCreatedAt());
        memberPoint.setUpdatedAt(memberPointDTO.getUpdatedAt());
        memberPoint.setIsActive(memberPointDTO.getIsActive());
        memberPoint.setPoint(memberPointDTO.getPoint());
        return memberPoint;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final MemberPoint memberPoint = memberPointRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        final Member memberPointMember = memberRepository.findFirstByMemberPoint(memberPoint);
        if (memberPointMember != null) {
            referencedWarning.setKey("memberPoint.member.memberPoint.referenced");
            referencedWarning.addParam(memberPointMember.getId());
            return referencedWarning;
        }
        return null;
    }

}
