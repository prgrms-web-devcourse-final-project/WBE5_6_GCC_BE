package com.honlife.core.app.model.member.service;

import com.honlife.core.app.controller.auth.payload.SignupBasicRequest;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.category.service.InterestCategoryService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

@RequiredArgsConstructor
@Service
public class MemberService {

    private final ModelMapper modelMapper;
    private final InterestCategoryService interestCategoryService;

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
    private final ModelMapper mapper;

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

    /**
     * 회원 테이블에 이미 존재하는 닉네임인지 확인
     * @param nickname 검사하고자 하는 닉네임
     * @return {@code Boolean}
     */
    public boolean isNicknameExists(final String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    /**
     * 회원 테이블에서 이미 존재하는 이메일인지 확인<br>
     * IgnoreCase - 대소문자 구분 없이 검색
     * @param email 사용자 이메일
     * @return {@code Boolean}
     */
    public boolean isEmailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    /**
     * 인증된 회원인지 확인
     * @param email 사용자 이메일
     * @return {@code Boolean}
     */
    public boolean isEmailVerified(final String email) {
        return memberRepository.isEmailVerified(email);
    }

    /**
     * 회원가입 phase 1 진행시 사용되는 매서드<br>
     * 회원 정보를 입력받아 {@code isActive = false} 인 상태로 테이블에 저장
     * @param signupBasicRequest 회원가입 단계에서 넘어오는 회원 정보 객체
     */
    @Transactional
    public void saveNotVerifiedMember(SignupBasicRequest signupBasicRequest) {
        Member member = new Member();
        modelMapper.map(signupBasicRequest, member);
        member.setIsActive(false);   // 이메일인증까지 완료되야 계정 활성화.
        member.setIsVerified(false);
        member.setNickname(signupBasicRequest.getName());
        member.setRole(Role.ROLE_USER);
        memberRepository.save(member);
    }

    /**
     * 회원의 계정 상태 정보를 업데이트 합니다.<br>
     * 인증상태와 계정 활성화 상태에 대한 {@code Boolean} 값을 받아, 계정 정보에 반영합니다.
     * @param email 회원 이메일
     * @param isVerified 이메일 인증 여부
     * @param isActive 계정 활성화 여부
     */
    @Transactional
    public void updateMemberStatus(String email, Boolean isVerified, Boolean isActive) {
        Member member = memberRepository.findByEmailIgnoreCase(email);
        member.setIsActive(isActive);
        member.setIsVerified(isVerified);
        memberRepository.save(member);
    }

    /**
     * 회원가입 phase 1 에서 가입을 재시도하거나 정보를 수정 하는 경우에 사용되는 매서드<br>
     * 기존에 회원가입을 눌렀을 경우 해당 회원정보가 DB에 저장되어있으므로, 업데이트 방식을 실행
     * @param signupBasicRequest 회원가입 단게에서 넘어오는 회원 정보 객체
     */
    @Transactional
    public void updateNotVerifiedMember(SignupBasicRequest signupBasicRequest) {
        // 회원정보 업데이트
        Member member = memberRepository.findByEmailIgnoreCase(signupBasicRequest.getEmail());
        modelMapper.map(signupBasicRequest, member);
    }


    /**
     * 회원의 이메일을 받아 회원 정보를 리턴하는 메소드
     * @param userEmail 현재 로그인한 회원의 이메일
     * @return 회원의 정보가 없다면 null을, 있다면 회원의 정보를 담은 dto를 반환합니다.
     */
    public MemberDTO findMemberByEmail(String userEmail){
        Member targetMember = memberRepository.findByEmailAndIsActive(userEmail, true).orElse(null);
        return mapper.map(targetMember, MemberDTO.class);
    }

}
