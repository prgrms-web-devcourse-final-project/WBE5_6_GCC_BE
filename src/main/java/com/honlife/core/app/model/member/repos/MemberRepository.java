package com.honlife.core.app.model.member.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicknameIgnoreCase(String nickname);

    Optional<Member> findByEmail(String email);

    /**
     * email과 isActive를 사용하여 기존의 회원정보를 검색하고 반환합니다.
     * @param email member의 email
     * @param isActive 활성화 상태
     * @return {@code Optianl<Member>}
     */
    Optional<Member> findByEmailAndIsActive(String email, Boolean isActive);
}
