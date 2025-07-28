package com.honlife.core.app.model.member.service;

import com.honlife.core.app.controller.auth.payload.SignupRequest;
import com.honlife.core.app.controller.member.payload.MemberWithdrawRequest;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.category.repos.InterestCategoryRepository;
import com.honlife.core.app.model.member.repos.MemberBadgeRepository;
import com.honlife.core.app.model.member.repos.MemberItemRepository;
import com.honlife.core.app.model.member.repos.MemberPointRepository;
import com.honlife.core.app.model.oauth2.service.GoogleUnlinkService;
import com.honlife.core.app.model.oauth2.service.KakaoUnlinkService;
import com.honlife.core.app.model.quest.repos.EventQuestProgressRepository;
import com.honlife.core.app.model.quest.repos.WeeklyQuestProgressRepository;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
import com.honlife.core.app.model.withdraw.service.WithdrawReasonService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.category.domain.Category;
import com.honlife.core.app.model.category.domain.InterestCategory;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberBadge;
import com.honlife.core.app.model.member.domain.MemberItem;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.quest.domain.WeeklyQuestProgress;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.infra.error.exceptions.ReferencedWarning;
import com.honlife.core.infra.error.exceptions.NotFoundException;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final WithdrawReasonService withdrawReasonService;
    private final MemberPointRepository memberPointRepository;
    private final NotificationRepository notificationRepository;
    private final RoutineRepository routineRepository;
    private final CategoryRepository categoryRepository;
    private final MemberItemRepository memberItemRepository;
    private final WeeklyQuestProgressRepository weeklyQuestProgressRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final InterestCategoryRepository interestCategoryRepository;
    private final EventQuestProgressRepository eventQuestProgressRepository;
    private final GoogleUnlinkService googleUnlinkService;
    private final KakaoUnlinkService kakaoUnlinkService;


    public MemberDTO get(final Long id) {
        return memberRepository.findById(id)
            .map(member -> mapToDTO(member, new MemberDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
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

    /**
     * 참조 무결성을 점검하고, 경고 메시지를 제공하는 사전 검증용 로직
     * @param userEmail
     * @return
     */
    public ReferencedWarning getReferencedWarning(final String userEmail) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Member member = memberRepository.findByEmailAndIsActive(userEmail,true)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        final Routine memberRoutine = routineRepository.findFirstByMemberAndIsActive(member,true);
        if (memberRoutine != null) {
            referencedWarning.setKey("member.routine.member.referenced");
            referencedWarning.addParam(memberRoutine.getId());
            return referencedWarning;
        }
        final Category memberCategory = categoryRepository.findFirstByMemberAndIsActive(member,true);
        if (memberCategory != null) {
            referencedWarning.setKey("member.category.member.referenced");
            referencedWarning.addParam(memberCategory.getId());
            return referencedWarning;
        }
        final MemberItem memberMemberItem = memberItemRepository.findFirstByMemberAndIsActive(member,true);
        if (memberMemberItem != null) {
            referencedWarning.setKey("member.memberItem.member.referenced");
            referencedWarning.addParam(memberMemberItem.getId());
            return referencedWarning;
        }
        final WeeklyQuestProgress memberWeeklyQuestProgress = weeklyQuestProgressRepository.findFirstByMemberAndIsActive(member, true);
        if (memberWeeklyQuestProgress != null) {
            referencedWarning.setKey("member.memberQuest.member.referenced");
            referencedWarning.addParam(memberWeeklyQuestProgress.getId());
            return referencedWarning;
        }
        final MemberBadge memberMemberBadge = memberBadgeRepository.findFirstByMemberAndIsActive(member, true);
        if (memberMemberBadge != null) {
            referencedWarning.setKey("member.memberBadge.member.referenced");
            referencedWarning.addParam(memberMemberBadge.getId());
            return referencedWarning;
        }
        final InterestCategory memberInterestCategory = interestCategoryRepository.findFirstByMemberAndIsActive(member, true);
        if (memberInterestCategory != null) {
            referencedWarning.setKey("member.interestCategory.member.referenced");
            referencedWarning.addParam(memberInterestCategory.getId());
            return referencedWarning;
        }
        final MemberPoint memberMemberPoint = memberPointRepository.findFirstByMemberAndIsActive(member, true);
        if (memberMemberPoint != null) {
            referencedWarning.setKey("member.memberPoint.member.referenced");
            referencedWarning.addParam(memberMemberPoint.getId());
            return referencedWarning;
        }
        return null;
    }

    /**
     * 존재하는 계정인지 확인<br>
     * 계정이 존재하지 않는다면 null 반환<br>
     * 만약 계정이 존재하지만, {@code isActive = false} 라면 회원탈퇴 또는 모종의 이유로 비활성화된 계정으로 판단.
     * @param email 회원 이메일
     * @return {@code Optional<Boolean>}
     */
    public Optional<Boolean> isActiveAccount(final String email) {
        Optional<Member> member = Optional.ofNullable(
            memberRepository.findByEmailIgnoreCase(email));
        if (member.isPresent()) {
            if(!member.get().getIsActive()){
                return Optional.of(false);
            }
            return Optional.of(true);
        } else {
            return Optional.empty();
        }
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
     * 회원 테이블에서 이미 존재하는 이메일인지 확인
     * @param email 사용자 이메일
     * @return {@code Boolean}
     */
    public Boolean isEmailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCaseAndIsActive(email, null);
    }

    /**
     * 회원 테이블에서 이미 존재하는 이메일인지 확인<br>
     * isActive 값을 넣으면 활성화 또는 비활성화 된 계정 정보 중 검색
     * @param email 사용자 이메일
     * @param isActive account activation status
     * @return {@code Boolean}
     */
    public Boolean isEmailExists(final String email, final Boolean isActive) {
        return memberRepository.existsByEmailIgnoreCaseAndIsActive(email, isActive);
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
     * @param signupRequest 회원가입 단계에서 넘어오는 회원 정보 객체
     */
    @Transactional
    public void saveNotVerifiedMember(SignupRequest signupRequest) {
        Member member = new Member();
        modelMapper.map(signupRequest, member);
        member.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // 암호화된 비밀번호 저장
        member.setNickname(signupRequest.getName());
        member.setRole(Role.ROLE_USER);
        memberRepository.save(member);

        MemberPoint memberPoint = new MemberPoint();
        memberPoint.setMember(member);
        memberPoint.setPoint(0);
        memberPointRepository.save(memberPoint);

        Notification notification = new Notification();
        notification.setMember(member);
        notificationRepository.save(notification);
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
     * @param signupRequest 회원가입 단게에서 넘어오는 회원 정보 객체
     */
    @Transactional
    public void updateNotVerifiedMember(SignupRequest signupRequest) {
        // 회원정보 업데이트
        Member member = memberRepository.findByEmailIgnoreCase(signupRequest.getEmail());
        modelMapper.map(signupRequest, member);
        member.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // 암호화된 비밀번호 저장
        memberRepository.save(member);
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

    /**
     * 사용자가 입력한 oldPassword의 값이 현재 DB에 저장되어 있는 password과 같은지 비교
     * @param userEmail 회원 이메일
     * @param oldPassword 회원이 입력한 기존 비밀 번호
     * @return {@code Boolean}
     */
    public Boolean isCorrectPassword(String userEmail, String oldPassword) {
        Optional<Member> user = memberRepository.findByEmailAndIsActive(userEmail,true);
        String savedPassword="";
        if(user.isPresent()){
            savedPassword = user.get().getPassword();
        }
        return passwordEncoder.matches(oldPassword, savedPassword);
    }

    /**
     * 비밀번호를 업데이트 합니다.
     * @param userEmail 유저 이메일
     * @param newPassword 새로 변경할 비밀번호
     * @return
     */
    @Transactional
    public void updatePassword(String userEmail, String newPassword) {
        Member targetMember = memberRepository
            .findByEmailAndIsActive(userEmail, true).orElseThrow(() -> new CommonException(
                ResponseCode.NOT_FOUND_MEMBER));

        targetMember.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(targetMember);
    }

    /**
     * 멤버 정보를 update 합니다. 관련 정보가 null로 넘어온 경우 기존의 데이터를 유지합니다.
     * @param userEmail 유저 이메일
     * @param updatedMemberDTO 유저의 새로운 정보가 담긴 DTO
     */
    @Transactional
    public void updateMember(String userEmail, MemberDTO updatedMemberDTO) {
        Member targetMember = memberRepository
            .findByEmailAndIsActive(userEmail, true).orElseThrow(() -> new CommonException(
                ResponseCode.NOT_FOUND_MEMBER));

        // 요청에 반드시 포함되는 필드
        targetMember.setName(updatedMemberDTO.getName());
        targetMember.setNickname(updatedMemberDTO.getNickname());

        // 관련 정보가 null로 넘어온 경우 기존의 데이터 유지
        if(updatedMemberDTO.getResidenceExperience()!=null){
            targetMember.setResidenceExperience(updatedMemberDTO.getResidenceExperience());
        }
        if(updatedMemberDTO.getRegion1Dept()!=null&&!updatedMemberDTO.getRegion1Dept().isBlank()){
            targetMember.setRegion1Dept(updatedMemberDTO.getRegion1Dept());
        }
        if(updatedMemberDTO.getRegion2Dept()!=null&&!updatedMemberDTO.getRegion2Dept().isBlank()){
            targetMember.setRegion2Dept(updatedMemberDTO.getRegion2Dept());
        }
        if(updatedMemberDTO.getRegion3Dept()!=null&&!updatedMemberDTO.getRegion3Dept().isBlank()){
            targetMember.setRegion3Dept(updatedMemberDTO.getRegion3Dept());
        }
        memberRepository.save(targetMember);
    }


    /**
     * 회원 탈퇴 시 탈퇴 이유를 저장하는 메소드
     * @param withdrawRequest 회원 탈퇴 이유를 저장하는 dto
     */
    @Transactional
    public void saveWithdrawReason(MemberWithdrawRequest withdrawRequest) {

        WithdrawReasonDTO withdraw = new WithdrawReasonDTO();

        withdraw.setType(withdrawRequest.getWithdrawType());
        withdraw.setReason(withdrawRequest.getEtcReason());

        withdrawReasonService.create(withdraw);

    }

    /**
     * 멤버를 삭제합니다.
     * @param userEmail 멤버 이메일
     */
    @Transactional
    public void softDropMember(String userEmail) {
        Member member = memberRepository.findByEmailAndIsActive(userEmail, true)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

        // 소셜 로그인 회원인 경우 연결 해체 진행
        if(member.getProvider().equals("google")){
            googleUnlinkService.unlink(member);
        } else if (member.getProvider().equals("kakao")){
            kakaoUnlinkService.unlink(member);
        }

        memberRepository.softDropMember(userEmail);
    }

    /**
     * 회원의 이메일을 받아 회원 정보를 리턴하는 메소드
     * @param userEmail 현재 로그인한 회원의 이메일
     * @return Memeber 엔티티값을 반환합니다
     */
    public Member getMemberByEmail(String userEmail){
        return memberRepository.findByEmailIgnoreCase(userEmail);
    }

    /**
     * 해당하는 멤버를 참조하는 모든 값들을 싹 soft drop시킵니다.
     * @param userEmail 멤버 이메일
     */
    @Transactional
    public void removeMemberReference(String userEmail) {
        Long memberId = findMemberByEmail(userEmail).getId();

        // 탈퇴하려는 멤버와 관련된 테이블 싹 다 soft drop
        // 루틴 is_active = false
        routineRepository.softDropByMemberId(memberId);
        // 카테고리 is_active = false
        categoryRepository.softDropByMemberId(memberId);
        // 멤버 아이템 is_active = false
        memberItemRepository.softDropByMemberId(memberId);
        // 주간 퀘스트 진행도 is active = false
        weeklyQuestProgressRepository.softDropByMemberId(memberId);
        // 이벤트 퀘스트 진행도 is active = false
        eventQuestProgressRepository.softDropByMemberId(memberId);
        // 멤버 업적 is_active = false
        memberBadgeRepository.softDropByMemberId(memberId);
        // 선호 카테고리 is_active = false
        interestCategoryRepository.softDropByMemberId(memberId);
        // 멤버 포인트 is_active = false
        memberPointRepository.softDropByMemberId(memberId);

    }
}
