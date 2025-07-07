package spring.grepp.honlife.app.model.notification.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.grepp.honlife.app.model.notification.domain.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
