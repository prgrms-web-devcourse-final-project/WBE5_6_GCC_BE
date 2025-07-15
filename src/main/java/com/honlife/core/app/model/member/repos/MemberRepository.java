package com.honlife.core.app.model.member.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicknameIgnoreCase(String nickname);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndIsActive(String email, Boolean isActive);
}
