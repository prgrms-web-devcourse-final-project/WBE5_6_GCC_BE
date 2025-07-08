package spring.grepp.honlife.app.model.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.member.domain.Member;
import spring.grepp.honlife.app.model.member.domain.MemberImage;
import spring.grepp.honlife.app.model.member.domain.MemberPoint;
import spring.grepp.honlife.app.model.notification.domain.Notification;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findFirstByMemberImage(MemberImage memberImage);

    Member findFirstByNotification(Notification notification);

    Member findFirstByMemberPoint(MemberPoint memberPoint);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsByMemberImageId(Long id);

    boolean existsByNotificationId(Long id);

    boolean existsByMemberPointId(Long id);

}
