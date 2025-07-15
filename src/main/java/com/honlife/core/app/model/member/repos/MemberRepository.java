package com.honlife.core.app.model.member.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    boolean existsByEmailIgnoreCase(String email);

    Optional<Member> findByEmail(String email);

    /**
     * 파라메터로 전달받은 닉네임과 동일한 닉네임이 `Member` 테이블에 존재하는지 검사합니다.
     * @param nickname 중복 검사할 닉네임
     * @return {@code Boolean}
     */
    boolean existsByNickname(String nickname);

    /**
     * email을 이용하여 기존의 회원정보를 검색하고 반환합니다.<br>
     * IgnoreCase : 대소문자 구별없이 검색함
     * @param email - 사용자 이메일
     * @return {@link Member}
     */
    Member findByEmailIgnoreCase(String email);
}
