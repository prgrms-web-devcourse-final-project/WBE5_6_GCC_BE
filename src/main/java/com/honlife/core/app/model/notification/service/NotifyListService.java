package com.honlife.core.app.model.notification.service;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.domain.NotifyList;
import com.honlife.core.app.model.notification.dto.NotifyListDTO;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.app.model.notification.repos.NotifyListRepository;
import com.honlife.core.app.model.websocket.service.NotificationSocketService;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NotifyListService {

  private final NotifyListRepository notifyListRepository;
  private final MemberRepository memberRepository;
  private final NotificationSocketService notificationSocketService;
  private final NotificationRepository notificationRepository;


  /**
   * 회원의 알림 단건 조회
   */
  @Transactional
  public void readNotification(String userEmail, Long notifyId) {

    NotifyList notifyList = notifyListRepository
        .findByMember_EmailAndId(userEmail, notifyId);

    notifyList.setIsRead(true);


  }

  /**
   * 회원의 알림 전체 조회
   */

  @Transactional
  public void readAllNotification(String userEmail) {

    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    List<NotifyList> notifyList = notifyListRepository
        .findByMember(member);

    for(NotifyList notify : notifyList){
      notify.setIsRead(true);
    }

  }

  /**
   * 회원의 이메일로 알림 전체 조회
   */
  public List<NotifyListDTO> getAllNotification(String userEmail) {

    List<NotifyList> notifyLists = notifyListRepository.findByMember_EmailAndIsReadFalse(userEmail);

    return notifyLists.stream()
        .map(n -> NotifyListDTO.builder()
            .id(n.getId())
            .type(n.getType())
            .name(n.getName())
            .createdAt(n.getCreatedAt())
            .updateAt(n.getUpdatedAt())
            .build())
        .toList();
  }

  @Transactional
  public void saveNotifyAndSendSocket(String userEmail, String name, NotificationType type) {
    Member member = memberRepository.findByEmail(userEmail)
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    Notification notification = notificationRepository.findByMember_Email(member.getEmail())
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

    if(isTypeTrue(type, notification)) {

      NotifyList notifyList = NotifyList.builder()
          .type(type)
          .name(name + "를 완료하였습니다")
          .isRead(false)
          .member(member)
          .build();

      notifyListRepository.save(notifyList);

      notificationSocketService.sendNotification(type, member.getEmail());


    }



  }

  @Transactional
  public void notifyIncompleteRoutines(Member member, long count) {


    Notification notification = notificationRepository.findByMember_Email(member.getEmail())
        .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));

    if(isTypeTrue(NotificationType.ROUTINE, notification)) {
      // 1. DB에 저장
      notifyListRepository.save(
          NotifyList.builder()
              .type(NotificationType.ROUTINE)
              .name("오늘 완료되지 않은 루틴이 " + count + "개 있습니다.")
              .isRead(false)
              .member(member)
              .build()
      );

      notificationSocketService.sendNotification(NotificationType.ROUTINE, member.getEmail());
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
