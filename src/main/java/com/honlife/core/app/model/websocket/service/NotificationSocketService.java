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

          messageSendingOperations.convertAndSend("/topic/notify/"+ userEmail,  "ping");

    }


}
