package spring.grepp.honlife.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import spring.grepp.honlife.category.domain.Category;
import spring.grepp.honlife.category.repos.CategoryRepository;
import spring.grepp.honlife.login_log.domain.LoginLog;
import spring.grepp.honlife.login_log.repos.LoginLogRepository;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.member.model.MemberDTO;
import spring.grepp.honlife.member.repos.MemberRepository;
import spring.grepp.honlife.member_badge.domain.MemberBadge;
import spring.grepp.honlife.member_badge.repos.MemberBadgeRepository;
import spring.grepp.honlife.member_image.domain.MemberImage;
import spring.grepp.honlife.member_image.repos.MemberImageRepository;
import spring.grepp.honlife.member_item.domain.MemberItem;
import spring.grepp.honlife.member_item.repos.MemberItemRepository;
import spring.grepp.honlife.member_quest.domain.MemberQuest;
import spring.grepp.honlife.member_quest.repos.MemberQuestRepository;
import spring.grepp.honlife.notification.domain.Notification;
import spring.grepp.honlife.notification.repos.NotificationRepository;
import spring.grepp.honlife.point_log.domain.PointLog;
import spring.grepp.honlife.point_log.repos.PointLogRepository;
import spring.grepp.honlife.routine.domain.Routine;
import spring.grepp.honlife.routine.repos.RoutineRepository;
import spring.grepp.honlife.util.NotFoundException;
import spring.grepp.honlife.util.ReferencedWarning;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final NotificationRepository notificationRepository;
    private final RoutineRepository routineRepository;
    private final CategoryRepository categoryRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final PointLogRepository pointLogRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final LoginLogRepository loginLogRepository;

    public MemberService(final MemberRepository memberRepository,
            final MemberImageRepository memberImageRepository,
            final NotificationRepository notificationRepository,
            final RoutineRepository routineRepository, final CategoryRepository categoryRepository,
            final MemberItemRepository memberItemRepository,
            final MemberQuestRepository memberQuestRepository,
            final PointLogRepository pointLogRepository,
            final MemberBadgeRepository memberBadgeRepository,
            final LoginLogRepository loginLogRepository) {
        this.memberRepository = memberRepository;
        this.memberImageRepository = memberImageRepository;
        this.notificationRepository = notificationRepository;
        this.routineRepository = routineRepository;
        this.categoryRepository = categoryRepository;
        this.memberItemRepository = memberItemRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.pointLogRepository = pointLogRepository;
        this.memberBadgeRepository = memberBadgeRepository;
        this.loginLogRepository = loginLogRepository;
    }

    public List<MemberDTO> findAll() {
        final List<Member> members = memberRepository.findAll(Sort.by("id"));
        return members.stream()
                .map(member -> mapToDTO(member, new MemberDTO()))
                .toList();
    }

    public MemberDTO get(final Integer id) {
        return memberRepository.findById(id)
                .map(member -> mapToDTO(member, new MemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MemberDTO memberDTO) {
        final Member member = new Member();
        mapToEntity(memberDTO, member);
        return memberRepository.save(member).getId();
    }

    public void update(final Integer id, final MemberDTO memberDTO) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final Integer id) {
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
        memberDTO.setGender(member.getGender());
        memberDTO.setRegionDept1(member.getRegionDept1());
        memberDTO.setRegionDept2(member.getRegionDept2());
        memberDTO.setRegionDept3(member.getRegionDept3());
        memberDTO.setMemberImage(member.getMemberImage() == null ? null : member.getMemberImage().getId());
        memberDTO.setNotification(member.getNotification() == null ? null : member.getNotification().getId());
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
        member.setGender(memberDTO.getGender());
        member.setRegionDept1(memberDTO.getRegionDept1());
        member.setRegionDept2(memberDTO.getRegionDept2());
        member.setRegionDept3(memberDTO.getRegionDept3());
        final MemberImage memberImage = memberDTO.getMemberImage() == null ? null : memberImageRepository.findById(memberDTO.getMemberImage())
                .orElseThrow(() -> new NotFoundException("memberImage not found"));
        member.setMemberImage(memberImage);
        final Notification notification = memberDTO.getNotification() == null ? null : notificationRepository.findById(memberDTO.getNotification())
                .orElseThrow(() -> new NotFoundException("notification not found"));
        member.setNotification(notification);
        return member;
    }

    public boolean emailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    public boolean nicknameExists(final String nickname) {
        return memberRepository.existsByNicknameIgnoreCase(nickname);
    }

    public boolean memberImageExists(final Integer id) {
        return memberRepository.existsByMemberImageId(id);
    }

    public boolean notificationExists(final Integer id) {
        return memberRepository.existsByNotificationId(id);
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
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
        return null;
    }

}
