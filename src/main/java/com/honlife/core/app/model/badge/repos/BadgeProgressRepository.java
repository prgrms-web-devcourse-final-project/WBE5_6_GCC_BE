package com.honlife.core.app.model.badge.repos;

import com.honlife.core.app.model.badge.code.ProgressType;
import com.honlife.core.app.model.badge.domain.BadgeProgress;
import com.honlife.core.app.model.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeProgressRepository extends JpaRepository<BadgeProgress, Long> {

    /**
     * 특정 회원의 특정 진행률 조회
     * @param memberId 회원 ID
     * @param progressType 진행률 타입 (CATEGORY, LOGIN)
     * @param progressKey 세부 식별자 (카테고리 ID, "DAILY" 등)
     * @return 진행률 정보
     */
    Optional<BadgeProgress> findByMemberIdAndProgressTypeAndProgressKey(
        Long memberId, ProgressType progressType, String progressKey);

    /**
     * 특정 회원의 모든 진행률 조회
     * @param memberId 회원 ID
     * @return 해당 회원의 모든 진행률 리스트
     */
    List<BadgeProgress> findByMemberId(Long memberId);

    /**
     * 특정 회원의 특정 타입 진행률들 조회
     * @param memberId 회원 ID
     * @param progressType 진행률 타입
     * @return 해당 타입의 모든 진행률 리스트
     */
    List<BadgeProgress> findByMemberIdAndProgressType(Long memberId, ProgressType progressType);

    /**
     * 특정 회원의 특정 진행률 존재 여부 확인
     * @param memberId 회원 ID
     * @param progressType 진행률 타입
     * @param progressKey 세부 식별자
     * @return 존재 여부
     */
    boolean existsByMemberIdAndProgressTypeAndProgressKey(
        Long memberId, ProgressType progressType, String progressKey);

    /**
     * 특정 회원 관련 진행률 조회 (Member 엔티티 활용)
     * @param member 회원 엔티티
     * @return 해당 회원의 모든 진행률 리스트
     */
    List<BadgeProgress> findByMember(Member member);

    /**
     * 활성화된 진행률만 조회
     * @param memberId 회원 ID
     * @return 활성화된 진행률 리스트
     */
    List<BadgeProgress> findByMemberIdAndIsActiveTrue(Long memberId);

    /**
     * 특정 회원과 연관된 첫번째 진행률 조회 (참조 무결성 체크용)
     * @param member 회원 엔티티
     * @return 첫번째 진행률
     */
    BadgeProgress findFirstByMember(Member member);
}
