package spring.grepp.honlife.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.app.model.category.domain.Category;
import spring.grepp.honlife.app.model.category.domain.InterestCategory;
import spring.grepp.honlife.app.model.category.repos.CategoryRepository;
import spring.grepp.honlife.app.model.category.repos.InterestCategoryRepository;
import spring.grepp.honlife.app.model.loginLog.domain.LoginLog;
import spring.grepp.honlife.app.model.loginLog.repos.LoginLogRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberBadge;
import spring.grepp.honlife.app.model.member.domain.MemberImage;
import spring.grepp.honlife.app.model.member.domain.MemberItem;
import spring.grepp.honlife.app.model.member.domain.MemberPoint;
import spring.grepp.honlife.app.model.member.domain.MemberQuest;
import spring.grepp.honlife.app.model.member.model.MemberDTO;
import spring.grepp.honlife.app.model.member.repos.MemberBadgeRepository;
import spring.grepp.honlife.app.model.member.repos.MemberImageRepository;
import spring.grepp.honlife.app.model.member.repos.MemberItemRepository;
import spring.grepp.honlife.app.model.member.repos.MemberPointRepository;
import spring.grepp.honlife.app.model.member.repos.MemberQuestRepository;
import spring.grepp.honlife.app.model.member.repos.MemberRepository;
import spring.grepp.honlife.app.model.notification.domain.Notification;
import spring.grepp.honlife.app.model.notification.repos.NotificationRepository;
import spring.grepp.honlife.app.model.point.domain.PointLog;
import spring.grepp.honlife.app.model.point.repos.PointLogRepository;
import spring.grepp.honlife.app.model.routine.domain.Routine;
import spring.grepp.honlife.app.model.routine.repos.RoutineRepository;
import spring.grepp.honlife.infra.util.ReferencedWarning;
import spring.grepp.honlife.infra.util.NotFoundException;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final NotificationRepository notificationRepository;
    private final MemberPointRepository memberPointRepository;
    private final RoutineRepository routineRepository;
    private final CategoryRepository categoryRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final LoginLogRepository loginLogRepository;
    private final InterestCategoryRepository interestCategoryRepository;

    public MemberService(final MemberRepository memberRepository,
        final MemberImageRepository memberImageRepository,
        final NotificationRepository notificationRepository,
        final MemberPointRepository memberPointRepository,
        final RoutineRepository routineRepository, final CategoryRepository categoryRepository,
        final MemberItemRepository memberItemRepository,
        final MemberQuestRepository memberQuestRepository,
        final PointLogRepository pointLogRepository,
        final MemberBadgeRepository memberBadgeRepository,
        final LoginLogRepository loginLogRepository,
        final InterestCategoryRepository interestCategoryRepository) {
        this.memberRepository = memberRepository;
        this.memberImageRepository = memberImageRepository;
        this.notificationRepository = notificationRepository;
        this.memberPointRepository = memberPointRepository;
        this.routineRepository = routineRepository;
        this.categoryRepository = categoryRepository;
        this.memberItemRepository = memberItemRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.pointLogRepository = pointLogRepository;
        this.memberBadgeRepository = memberBadgeRepository;
        this.loginLogRepository = loginLogRepository;
        this.interestCategoryRepository = interestCategoryRepository;
    }

    public List<MemberDTO> findAll() {
        final List<Member> members = memberRepository.findAll(Sort.by("id"));
        return members.stream()
            .map(member -> mapToDTO(member, new MemberDTO()))
            .toList();
    }

    public MemberDTO get(final Long id) {
        return memberRepository.findById(id)
            .map(member -> mapToDTO(member, new MemberDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public Long create(final MemberDTO memberDTO) {
        final Member member = new Member();
        mapToEntity(memberDTO, member);
        return memberRepository.save(member).getId();
    }

    public void update(final Long id, final MemberDTO memberDTO) {
        final Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final Long id) {
        memberRepository.deleteById(id);
    }

    private MemberDTO mapToDTO(final Member member, final MemberDTO memberDTO) {
        memberDTO.setCreatedAt(member.getCreatedAt());
        memberDTO.setUpdatedAt(member.getUpdatedAt());
        memberDTO.setIsActive(member.getIsActive());
        memberDTO.setId(member.getId());
        memberDTO.setRole(member.getRole());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setName(member.getName());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setResidenceExperience(member.getResidenceExperience());
        memberDTO.setRegionDept1(member.getRegionDept1());
        memberDTO.setRegionDept2(member.getRegionDept2());
        memberDTO.setRegionDept3(member.getRegionDept3());
        memberDTO.setMemberImage(member.getMemberImage() == null ? null : member.getMemberImage().getId());
        memberDTO.setNotification(member.getNotification() == null ? null : member.getNotification().getId());
        memberDTO.setMemberPoint(member.getMemberPoint() == null ? null : member.getMemberPoint().getId());
        return memberDTO;
    }

    private Member mapToEntity(final MemberDTO memberDTO, final Member member) {
        member.setCreatedAt(memberDTO.getCreatedAt());
        member.setUpdatedAt(memberDTO.getUpdatedAt());
        member.setIsActive(memberDTO.getIsActive());
        member.setRole(memberDTO.getRole());
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setName(memberDTO.getName());
        member.setNickname(memberDTO.getNickname());
        member.setResidenceExperience(memberDTO.getResidenceExperience());
        member.setRegionDept1(memberDTO.getRegionDept1());
        member.setRegionDept2(memberDTO.getRegionDept2());
        member.setRegionDept3(memberDTO.getRegionDept3());
        final MemberImage memberImage = memberDTO.getMemberImage() == null ? null : memberImageRepository.findById(memberDTO.getMemberImage())
            .orElseThrow(() -> new NotFoundException("memberImage not found"));
        member.setMemberImage(memberImage);
        final Notification notification = memberDTO.getNotification() == null ? null : notificationRepository.findById(memberDTO.getNotification())
            .orElseThrow(() -> new NotFoundException("notification not found"));
        member.setNotification(notification);
        final MemberPoint memberPoint = memberDTO.getMemberPoint() == null ? null : memberPointRepository.findById(memberDTO.getMemberPoint())
            .orElseThrow(() -> new NotFoundException("memberPoint not found"));
        member.setMemberPoint(memberPoint);
        return member;
    }

    public boolean emailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    public boolean nicknameExists(final String nickname) {
        return memberRepository.existsByNicknameIgnoreCase(nickname);
    }

    public boolean memberImageExists(final Long id) {
        return memberRepository.existsByMemberImageId(id);
    }

    public boolean notificationExists(final Long id) {
        return memberRepository.existsByNotificationId(id);
    }

    public boolean memberPointExists(final Long id) {
        return memberRepository.existsByMemberPointId(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        final Routine memberRoutine = routineRepository.findFirstByMember(member);
        if (memberRoutine != null) {
            referencedWarning.setKey("member.routine.member.referenced");
            referencedWarning.addParam(memberRoutine.getId());
            return referencedWarning;
        }
        final Category memberCategory = categoryRepository.findFirstByMember(member);
        if (memberCategory != null) {
            referencedWarning.setKey("member.category.member.referenced");
            referencedWarning.addParam(memberCategory.getId());
            return referencedWarning;
        }
        final MemberItem memberMemberItem = memberItemRepository.findFirstByMember(member);
        if (memberMemberItem != null) {
            referencedWarning.setKey("member.memberItem.member.referenced");
            referencedWarning.addParam(memberMemberItem.getId());
            return referencedWarning;
        }
        final MemberQuest memberMemberQuest = memberQuestRepository.findFirstByMember(member);
        if (memberMemberQuest != null) {
            referencedWarning.setKey("member.memberQuest.member.referenced");
            referencedWarning.addParam(memberMemberQuest.getId());
            return referencedWarning;
        }
        final PointLog memberPointLog = pointLogRepository.findFirstByMember(member);
        if (memberPointLog != null) {
            referencedWarning.setKey("member.pointLog.member.referenced");
            referencedWarning.addParam(memberPointLog.getId());
            return referencedWarning;
        }
        final MemberBadge memberMemberBadge = memberBadgeRepository.findFirstByMember(member);
        if (memberMemberBadge != null) {
            referencedWarning.setKey("member.memberBadge.member.referenced");
            referencedWarning.addParam(memberMemberBadge.getId());
            return referencedWarning;
        }
        final LoginLog memberLoginLog = loginLogRepository.findFirstByMember(member);
        if (memberLoginLog != null) {
            referencedWarning.setKey("member.loginLog.member.referenced");
            referencedWarning.addParam(memberLoginLog.getId());
            return referencedWarning;
        }
        final InterestCategory memberInterestCategory = interestCategoryRepository.findFirstByMember(member);
        if (memberInterestCategory != null) {
            referencedWarning.setKey("member.interestCategory.member.referenced");
            referencedWarning.addParam(memberInterestCategory.getId());
            return referencedWarning;
        }
        return null;
    }

}
