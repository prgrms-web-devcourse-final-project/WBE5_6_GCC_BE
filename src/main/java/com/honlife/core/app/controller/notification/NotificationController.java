package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import com.honlife.core.app.controller.notification.wrapper.NotificationWrapper;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.notification.service.NotificationService;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "🔄 [회원] 설정",description = "알림 설정관련 API")
@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 알림설정 변경 API
     * @param userDetails 인증 정보
     * @return {@code Void}
     */
    @Operation(summary = "🔄 알림 설정 변경", description = "로그인한 유저의 알림 설정을 변경합니다."
        + "<br>특정 알림 설정만 변경되더라도, 모든 알림 설정 정보를 담아 요청해야 합니다.")
    @PutMapping
    public ResponseEntity<CommonApiResponse<Void>> onOffNotification(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid NotificationPayload notificationPayload
        ){
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 유저의 현재 알림 상황조회 API
     * @param userDetails 유저의 인증정보
     * @return {@link NotificationWrapper}
     */
    @Operation(summary = "🔄 알림 설정 조회", description = "로그인한 유저의 알림 설정을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<NotificationWrapper>> getAllNotifications(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if(userDetails.getUsername().equals("user01@test.com")){
            return ResponseEntity.ok(CommonApiResponse.success(
                new NotificationWrapper(
                    NotificationPayload.builder()
                    .isEmail(true)
                    .isRoutine(false)
                    .isBadge(true).build()
                )
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonApiResponse.error(
            ResponseCode.INTERNAL_SERVER_ERROR));
    }

}