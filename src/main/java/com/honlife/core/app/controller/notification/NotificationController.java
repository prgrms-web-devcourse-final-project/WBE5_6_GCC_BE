package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import com.honlife.core.app.controller.notification.wrapper.NotificationWrapper;
import com.honlife.core.infra.response.CommonApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.notification.service.NotificationService;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/settings/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림설정 변경 API
     * @param userDetails 인증 정보
     * @param notificationPayload 변경된 설정 값 정보
     * @return {@code Void}
     */
    @PutMapping
    public ResponseEntity<CommonApiResponse<Void>> updateNotificationSettings(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid NotificationPayload notificationPayload
    ) {
        String userEmail = userDetails.getUsername();
        notificationService.updateNotification(userEmail, notificationPayload);
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 유저의 현재 알림 상황조회 API
     *
     * @param userDetails 유저의 인증정보
     * @return {@link NotificationPayload}
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<NotificationWrapper>> getNotificationSettings(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        NotificationPayload notificationPayload = NotificationPayload.fromDTO(
            notificationService.getNotification(userEmail)
        );
        return ResponseEntity.ok().body(CommonApiResponse.success(new NotificationWrapper(notificationPayload)));
    }

}