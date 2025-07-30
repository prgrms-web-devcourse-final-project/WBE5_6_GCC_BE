package com.honlife.core.app.model.websocket.service;


import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSocketService {

  private final NotificationRepository notificationRepository;
  private final SimpMessageSendingOperations messageSendingOperations;

  /**
   * 웹소켓 알림 전송 (유저 이메일 기준)
   * @param userEmail 대상 사용자
   */

    public void sendNotification(NotificationType type, String userEmail){

      Notification notification = notificationRepository.findByMember_Email(userEmail)
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

        if(isTypeTrue(type, notification)){

          String message = "새로운 " + type + "알림이 도착했습니다.";

          messageSendingOperations.convertAndSend("topic/notify",message);
        }
    }

    public Boolean isTypeTrue(NotificationType type, Notification notification){
        switch (type) {
          case NotificationType.QUEST:
                return Boolean.TRUE.equals(notification.getIsQuest());
          case NotificationType.ROUTINE:
                return Boolean.TRUE.equals(notification.getIsRoutine());
          case NotificationType.BADGE:
                return Boolean.TRUE.equals(notification.getIsBadge());
            default:
                return false;
        }
    }
}
