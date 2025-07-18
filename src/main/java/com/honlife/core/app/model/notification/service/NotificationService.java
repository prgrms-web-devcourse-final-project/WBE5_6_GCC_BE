package com.honlife.core.app.model.notification.service;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import com.honlife.core.app.model.member.repos.MemberRepository;
import com.honlife.core.infra.response.ResponseCode;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.member.domain.Member;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.app.model.notification.dto.NotificationDTO;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.error.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import com.honlife.core.app.model.notification.repos.NotificationRepository;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NotificationDTO get(final Long id) {
        return notificationRepository.findById(id)
            .map(notification -> mapToDTO(notification, new NotificationDTO()))
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_NOTIFICATION));
    }

    private NotificationDTO mapToDTO(final Notification notification,
        final NotificationDTO notificationDTO) {
        notificationDTO.setId(notification.getId());
        notificationDTO.setIsEmail(notification.getIsEmail());
        notificationDTO.setIsRoutine(notification.getIsRoutine());
        notificationDTO.setIsBadge(notification.getIsBadge());
        notificationDTO.setMember(notification.getMember() == null ? null : notification.getMember().getId());
        return notificationDTO;
    }

    private Notification mapToEntity(final NotificationDTO notificationDTO,
        final Notification notification) {
        notification.setIsEmail(notificationDTO.getIsEmail());
        notification.setIsRoutine(notificationDTO.getIsRoutine());
        notification.setIsBadge(notificationDTO.getIsBadge());
        final Member member = notificationDTO.getMember() == null ? null : memberRepository.findById(notificationDTO.getMember())
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_MEMBER));
        notification.setMember(member);
        return notification;
    }

    public boolean memberExists(final Long id) {
        return notificationRepository.existsByMemberId(id);
    }

    /**
     * 회원의 알림 설정 업데이트<br>
     * {@code @Transactional}로 인해 로직이 끝나는 시점에서 업데이트 됩니다.
     * @param userEmail           회원 이메일
     * @param notificationPayload 설정 값 정보
     */
    @Transactional
    public void updateNotification(String userEmail, NotificationPayload notificationPayload) {
        Notification notification = notificationRepository.findByMember_Email(userEmail)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        notification.setIsEmail(notificationPayload.getIsEmail());
        notification.setIsRoutine(notificationPayload.getIsRoutine());
        notification.setIsBadge(notificationPayload.getIsBadge());
    }

    /**
     * 회원의 알림 설정 정보 확인
     * @param userEmail 회원 이메일
     * @return {@link NotificationDTO}
     */
    public NotificationDTO getNotification(String userEmail) {
        Notification notification = notificationRepository.findByMember_Email(userEmail)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        return NotificationDTO.builder()
            .isRoutine(notification.getIsRoutine())
            .isBadge(notification.getIsBadge())
            .isEmail(notification.getIsEmail())
            .build();
    }
}
