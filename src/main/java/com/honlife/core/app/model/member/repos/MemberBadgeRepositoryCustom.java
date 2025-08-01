package com.honlife.core.app.model.member.repos;

import com.honlife.core.app.model.member.domain.MemberBadge;
import java.util.Optional;

public interface MemberBadgeRepositoryCustom {

    /**
     * 멤버 아이디를 통해 멤버 뱃지를 소프트 드랍하는 메소드
     * @param memberId 멤버 식별 아이디
     */
    void softDropByMemberId(Long memberId);

    /**
     * 장착한 뱃지 정보를 가져옵니다.
     * @param userEmail 멤버 이메일
     * @param IsEquipped 장착 여부
     * @return Optional<MemberBadge>
     */
    Optional<MemberBadge> findByMemberIsEquipped(String userEmail, boolean IsEquipped);
}
