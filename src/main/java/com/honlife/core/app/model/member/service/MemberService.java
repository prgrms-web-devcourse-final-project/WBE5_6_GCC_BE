package com.honlife.core.app.model.member.service;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.domain.InterestCategory;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.category.repos.InterestCategoryRepository;
import com.honlife.core.app.model.loginLog.domain.LoginLog;
import com.honlife.core.app.model.loginLog.repos.LoginLogRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.member.domain.MemberQuest;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.member.repos.MemberQuestRepository;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.app.model.point.domain.PointLog;
import com.honlife.core.app.model.point.repos.PointLogRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.infra.util.ReferencedWarning;
import com.honlife.core.infra.util.NotFoundException;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final CategoryRepository categoryRepository;
    private final MemberItemRepository memberItemRepository;
    private final MemberQuestRepository memberQuestRepository;
    private final PointLogRepository pointLogRepository;
    private final NotificationRepository notificationRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final LoginLogRepository loginLogRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final MemberPointRepository memberPointRepository;

    public MemberService(final MemberRepository memberRepository,
        final RoutineRepository routineRepository, final CategoryRepository categoryRepository,
        final MemberItemRepository memberItemRepository,
        final MemberQuestRepository memberQuestRepository,
        final PointLogRepository pointLogRepository,
        final NotificationRepository notificationRepository,
        final MemberBadgeRepository memberBadgeRepository,
        final LoginLogRepository loginLogRepository,
        final InterestCategoryRepository interestCategoryRepository,
        final MemberPointRepository memberPointRepository) {
        this.memberRepository = memberRepository;
        this.routineRepository = routineRepository;
        this.categoryRepository = categoryRepository;
        this.memberItemRepository = memberItemRepository;
        this.memberQuestRepository = memberQuestRepository;
        this.pointLogRepository = pointLogRepository;
        this.notificationRepository = notificationRepository;
        this.memberBadgeRepository = memberBadgeRepository;
        this.loginLogRepository = loginLogRepository;
        this.interestCategoryRepository = interestCategoryRepository;
        this.memberPointRepository = memberPointRepository;
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
        memberDTO.setRegion1Dept(member.getRegion1Dept());
        memberDTO.setRegion2Dept(member.getRegion2Dept());
        memberDTO.setRegion3Dept(member.getRegion3Dept());
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
        member.setRegion1Dept(memberDTO.getRegion1Dept());
        member.setRegion2Dept(memberDTO.getRegion2Dept());
        member.setRegion3Dept(memberDTO.getRegion3Dept());
        return member;
    }

    public boolean emailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    public boolean nicknameExists(final String nickname) {
        return memberRepository.existsByNicknameIgnoreCase(nickname);
    }

    /**
     * 참조 무결성을 점검하고, 경고 메시지를 제공하는 사전 검증용 로직
     * @param id
     * @return
     */
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
        final Notification memberNotification = notificationRepository.findFirstByMember(member);
        if (memberNotification != null) {
            referencedWarning.setKey("member.notification.member.referenced");
            referencedWarning.addParam(memberNotification.getId());
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
        final MemberPoint memberMemberPoint = memberPointRepository.findFirstByMember(member);
        if (memberMemberPoint != null) {
            referencedWarning.setKey("member.memberPoint.member.referenced");
            referencedWarning.addParam(memberMemberPoint.getId());
            return referencedWarning;
        }
        return null;
    }

}
