package com.honlife.core.app.model.notification.repos;

import com.honlife.core.app.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import com.honlife.core.app.model.notification.domain.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findFirstByMember(Member member);

    boolean existsByMemberId(Long id);

}
