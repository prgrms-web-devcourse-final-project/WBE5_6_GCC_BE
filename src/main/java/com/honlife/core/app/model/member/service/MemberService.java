package com.honlife.core.app.model.member.service;

import com.honlife.core.app.controller.auth.payload.SignupRequest;
import com.honlife.core.app.controller.member.payload.MemberWithdrawRequest;
import com.honlife.core.app.model.auth.code.Role;
import com.honlife.core.app.model.category.service.CategoryService;
import com.honlife.core.app.model.category.service.InterestCategoryService;
import com.honlife.core.app.model.routine.service.RoutineService;
import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
import com.honlife.core.app.model.withdraw.service.WithdrawReasonService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
import com.honlife.core.app.model.category.repos.CategoryRepository;
import com.honlife.core.app.model.category.repos.InterestCategoryRepository;
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
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.app.model.point.repos.PointLogRepository;
import com.honlife.core.app.model.routine.domain.Routine;
import com.honlife.core.app.model.routine.repos.RoutineRepository;
import com.honlife.core.infra.util.ReferencedWarning;
import com.honlife.core.infra.util.NotFoundException;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final ModelMapper modelMapper;
    private final InterestCategoryService interestCategoryService;

    private final MemberRepository memberRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final WithdrawReasonService withdrawReasonService;
    private final RoutineService routineService;
    private final CategoryService categoryService;
    private final MemberItemService memberItemService;
    private final MemberQuestService memberQuestService;
    private final MemberBadgeService memberBadgeService;
    private final MemberPointService memberPointService;

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
     * @param userEmail
     * @return
     */
    public ReferencedWarning getReferencedWarning(final String userEmail) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Member member = memberRepository.findByEmailAndIsActive(userEmail,true)
            .orElseThrow(NotFoundException::new);
        final Routine memberRoutine = routineService.findFirstRoutineByMemberAndIsActive(member,true);
        if (memberRoutine != null) {
            referencedWarning.setKey("member.routine.member.referenced");
            referencedWarning.addParam(memberRoutine.getId());
            return referencedWarning;
        }
        final Category memberCategory = categoryService.findFirstCategoryByMemberAndIsActive(member,true);
        if (memberCategory != null) {
            referencedWarning.setKey("member.category.member.referenced");
            referencedWarning.addParam(memberCategory.getId());
            return referencedWarning;
        }
        final MemberItem memberMemberItem = memberItemService.findFirstMemberItemByMemberAndIsActive(member,true);
        if (memberMemberItem != null) {
            referencedWarning.setKey("member.memberItem.member.referenced");
            referencedWarning.addParam(memberMemberItem.getId());
            return referencedWarning;
        }
        final MemberQuest memberMemberQuest = memberQuestService.findFirstMemberQuestByMemberAndIsActive(member, true);
        if (memberMemberQuest != null) {
            referencedWarning.setKey("member.memberQuest.member.referenced");
            referencedWarning.addParam(memberMemberQuest.getId());
            return referencedWarning;
        }
        final MemberBadge memberMemberBadge = memberBadgeService.findFirstMemberBadgeByMemberAndIsActive(member, true);
        if (memberMemberBadge != null) {
            referencedWarning.setKey("member.memberBadge.member.referenced");
            referencedWarning.addParam(memberMemberBadge.getId());
            return referencedWarning;
        }
        final InterestCategory memberInterestCategory = interestCategoryService.findFirstInterestCategoryByMemberAndIsActive(member, true);
        if (memberInterestCategory != null) {
            referencedWarning.setKey("member.interestCategory.member.referenced");
            referencedWarning.addParam(memberInterestCategory.getId());
            return referencedWarning;
        }
        final MemberPoint memberMemberPoint = memberPointService.findFirstMemberPointByMemberAndIsActive(member, true);
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
     * 회원가입시 사용되는 매서드<br>
     * 회원 정보를 입력받아 {@code isActive = false} 인 상태로 테이블에 저장
     * @param signupRequest 회원가입 단계에서 넘어오는 회원 정보 객체
     */
    @Transactional
    public void saveNotVerifiedMember(SignupRequest signupRequest) {
        Member member = new Member();
        modelMapper.map(signupRequest, member);
        member.setIsActive(false);   // 회원가입 완료후 계정 활성화
        member.setIsVerified(false);
        member.setRole(Role.ROLE_USER);
        memberRepository.save(member);

        // 관심 카테고리 정보 저장
        interestCategoryService.saveInterestCategory(member, signupRequest.getInterestedCategoryIds());
    }

    /**
     * 회원가입을 재시도 하는 경우에 사용되는 매서드<br>
     * 기존에 회원가입을 눌렀을 경우 해당 회원정보가 DB에 저장되어있으므로, 업데이트 방식을 실행<br>
     * 기존에 저장된 회원 정보를 수정하고, 관심카테고리 항목도 업데이트
     * @param signupRequest 회원가입 단게에서 넘어오는 회원 정보 객체
     */
    @Transactional
    public void updateNotVerifiedMember(SignupRequest signupRequest) {
        // 회원정보 업데이트
        Member member = memberRepository.findByEmailIgnoreCase(signupRequest.getEmail());
        modelMapper.map(signupRequest, member);

        // 관심카테고리 정보 업데이트
        interestCategoryService.updateInterestCategory(member, signupRequest.getInterestedCategoryIds());
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
    public Boolean isCorrectPassword(String userEmail, @NotBlank String oldPassword) {
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
    public void updatePassword(String userEmail, @NotBlank String newPassword) {
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
    public void saveWithdrawReason(@Valid MemberWithdrawRequest withdrawRequest) {

        WithdrawReasonDTO withdraw = new WithdrawReasonDTO();

        withdraw.setType(withdrawRequest.getWithdrawType());
        withdraw.setReason(withdrawRequest.getEtcReason());

        withdrawReasonService.create(withdraw);

    }

    public void deleteRelatedToMember(String userEmail) {

        Long memberId = memberRepository.findByEmailAndIsActive(userEmail,true).get().getId();

        // 루틴 is_active = false
        routineService.deleteRoutineByMemberId(memberId);
        // 카테고리 is_active = false
        categoryService.deleteCategoryByMemberId(memberId);
        // 멤버 아이템 is_active = false
        memberItemService.deleteMemberItemByMemberId(memberId);
        // 멤버 퀘스트 is active = false
        memberQuestService.deleteMemberQuestByMemberId(memberId);
        // 멤버 업적 is_active = false
        memberBadgeService.deleteMemberBadgeByMemberId(memberId);
        // 선호 카테고리 is_active = false
        interestCategoryService.deleteInterestCategoryByMemberId(memberId);
        // 멤버 포인트 is_active = false
        memberPointService.deleteMemberPointByMemberId(memberId);
    }

    /**
     * 멤버를 삭제합니다.
     * @param userEmail 멤버 이메일
     */
    @Transactional
    public void deleteMember(String userEmail) {
        memberRepository.deleteMember(userEmail);
    }
}
