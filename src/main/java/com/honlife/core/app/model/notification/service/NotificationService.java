package com.honlife.core.app.model.notification.service;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import com.honlife.core.app.model.notification.domain.Notification;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.honlife.core.app.model.notification.repos.NotificationRepository;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    /**
     * 회원의 알림 설정 업데이트<br> {@code @Transactional}로 인해 modelMapper만 사용해도 로직이 끝나는 시점에서 업데이트 됩니다.
     *
     * @param userEmail           회원 이메일
     * @param notificationPayload 설정 값 정보
     */
    @Transactional
    public void updateNotification(String userEmail, NotificationPayload notificationPayload) {
        Notification notification = notificationRepository.findByMember_Email(userEmail)
            .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        modelMapper.map(notificationPayload, notification);
    }
}
