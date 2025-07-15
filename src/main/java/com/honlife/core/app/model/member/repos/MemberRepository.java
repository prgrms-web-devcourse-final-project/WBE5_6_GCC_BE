package com.honlife.core.app.model.member.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicknameIgnoreCase(String nickname);

    Optional<Member> findByEmail(String email);

    boolean findIsVerifiedByEmailIgnoreCase(String email);

    /**
     * email을 이용하여 기존의 회원정보를 검색하고 반환합니다.<br>
     * IgnoreCase : 대소문자 구별없이 검색함
     * @param email - 사용자 이메일
     * @return {@link Member}
     */
    Member findByEmailIgnoreCase(String email);
}
