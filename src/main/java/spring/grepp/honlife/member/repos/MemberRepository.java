package spring.grepp.honlife.member.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.member.domain.Member;
import spring.grepp.honlife.notification.domain.Notification;


public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findFirstByNotification(Notification notification);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsByMemberImageId(Integer id);

    boolean existsByNotificationId(Integer id);

}
