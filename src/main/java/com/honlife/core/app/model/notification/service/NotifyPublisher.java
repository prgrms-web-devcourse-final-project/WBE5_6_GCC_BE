package com.honlife.core.app.model.notification.service;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.domain.NotifyList;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.app.model.notification.repos.NotifyListRepository;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotifyPublisher {

  private final NotifyListRepository notifyListRepository;
  private final NotificationRepository notificationRepository;
  private final MemberRepository memberRepository;
  private final SseService sseService;

  @Transactional
  public void saveNotifyAndSendSse(String userEmail, String name, NotificationType type) {
    try {
      Member member = memberRepository.findByEmail(userEmail)
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

      Notification notification = notificationRepository.findByMember_Email(member.getEmail())
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

      if (isTypeTrue(type, notification)) {
        NotifyList notifyList = NotifyList.builder()
            .type(type)
            .name(name + "를 완료하였습니다")
            .isRead(false)
            .member(member)
            .build();

        notifyListRepository.save(notifyList);

        sseService.sendTo(member.getId());
      }

    } catch (CommonException e) {// SSE 연결 유지한 채 silent fail 처리
    }
  }


  @Transactional
  public void notifyIncompleteRoutines(Member member, long count) {
    try {
      Notification notification = notificationRepository.findByMember_Email(member.getEmail())
          .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

      if (isTypeTrue(NotificationType.ROUTINE, notification)) {
        notifyListRepository.save(
            NotifyList.builder()
                .type(NotificationType.ROUTINE)
                .name("오늘 완료되지 않은 루틴이 " + count + "개 있습니다.")
                .isRead(false)
                .member(member)
                .build()
        );

        sseService.sendTo(member.getId());
      }

    } catch (CommonException e) {
      // 필요한 경우 SSE 연결은 유지하고 silent fail
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
