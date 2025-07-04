package spring.grepp.honlife.notification.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.notification.domain.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
