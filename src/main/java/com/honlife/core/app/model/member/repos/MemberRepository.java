package com.honlife.core.app.model.member.repos;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.domain.MemberImage;
import com.honlife.core.app.model.member.domain.MemberPoint;
import com.honlife.core.app.model.notification.domain.Notification;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findFirstByMemberImage(MemberImage memberImage);

    Member findFirstByNotification(Notification notification);

    Member findFirstByMemberPoint(MemberPoint memberPoint);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsByMemberImageId(Long id);

    boolean existsByNotificationId(Long id);

    boolean existsByMemberPointId(Long id);

    Optional<Member> findByEmail(String email);
}
