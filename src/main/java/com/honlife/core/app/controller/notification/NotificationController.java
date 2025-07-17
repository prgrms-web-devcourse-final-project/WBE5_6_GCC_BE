package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.notification.service.NotificationService;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림설정 변경 API
     * @param userDetails 인증 정보
     * @param notificationPayload 변경된 설정 값 정보
     * @return {@code Void}
     */
    @PutMapping
    public ResponseEntity<CommonApiResponse<Void>> onOffNotification(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NotificationPayload notificationPayload
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
    public ResponseEntity<CommonApiResponse<NotificationPayload>> getAllNotifications(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails.getUsername().equals("user01@test.com")) {
            return ResponseEntity.ok(CommonApiResponse.success(
                NotificationPayload.builder()
                    .isEmail(true)
                    .isRoutine(false)
                    .isBadge(true).build()
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonApiResponse.error(
            ResponseCode.INTERNAL_SERVER_ERROR));
    }

}