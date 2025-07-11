package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotificationResponse;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "설정",description = "설정관련 API 입니다.")
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
     * @param isEmail 이메일 알림 설정 값
     * @param isRoutine 루틴 알림 설정 값
     * @param isChallenge 업적 알림 설정 값
     * @return {@code Void}
     */
    @Operation(summary = "알림 설정 변경", description = "로그인한 유저의 알림 설정을 변경합니다."
        + "<br>파라메터는 모두 `required=false`입니다. 변경된 경우에만 파라메터에 담아 요청해주세요.")
    @PutMapping
    public ResponseEntity<CommonApiResponse<Void>> onOffNotification(
            @AuthenticationPrincipal UserDetails userDetails,
            @Schema(description = "이메일 알림 설정", example = "false")
            @RequestParam(name="email", required = false) final Boolean isEmail,
            @Schema(description = "루틴 알림 설정", example = "true")
            @RequestParam(name="routine", required = false) final Boolean isRoutine,
            @Schema(description = "업적 알림 설정", example = "false")
            @RequestParam(name="challenge", required = false) final Boolean isChallenge
            ){
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 유저의 현재 알림 상황조회 API
     * @param userDetails 유저의 인증정보
     * @return {@link NotificationResponse}
     */
    @Operation(summary = "알림 설정 조회", description = "로그인한 유저의 알림 설정을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<NotificationResponse>> getAllNotifications(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        if(userDetails.getUsername().equals("user01@test.com")){
            return ResponseEntity.ok(CommonApiResponse.success(
                NotificationResponse.builder()
                    .notificationId(10000L)
                    .isEmail(true)
                    .isRoutine(false)
                    .isChallenge(true).build()
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonApiResponse.error(
            ResponseCode.INTERNAL_SERVER_ERROR));
    }

}