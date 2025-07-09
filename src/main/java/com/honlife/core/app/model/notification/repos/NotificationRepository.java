package com.honlife.core.app.model.notification.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.notification.domain.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
