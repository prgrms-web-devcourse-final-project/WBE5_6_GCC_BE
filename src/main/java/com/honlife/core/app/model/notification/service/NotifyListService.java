package com.honlife.core.app.model.notification.service;

import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.member.model.MemberDTO;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.app.model.notification.domain.NotifyList;
import com.honlife.core.app.model.notification.dto.NotifyListDTO;
import com.honlife.core.app.model.notification.repos.NotificationRepository;
import com.honlife.core.app.model.notification.repos.NotifyListRepository;
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




  public void markAllAsRead(MemberDTO member) {

    Member memberEntity = memberRepository.findById(member.getId())
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND_MEMBER));

    notifyListRepository.markAllAsReadByMember(memberEntity);
  }

  public boolean hasUnread(Long memberId) {
    return notifyListRepository.existsByMemberIdAndIsReadFalse(memberId);
  }

}
