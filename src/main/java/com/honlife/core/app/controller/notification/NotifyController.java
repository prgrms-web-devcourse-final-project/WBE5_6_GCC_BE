package com.honlife.core.app.controller.notification;

import com.honlife.core.app.controller.notification.payload.NotifyResponse;
import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SecurityRequirement(name = "breathAuth")
@Tag(name = "[회원] 알림 목록",description = "알림 목록 관련 API")
@RestController
@RequestMapping(value = "/api/v1/notify", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotifyController {

    /**
     * 선택한 알림만 읽음 처리하는 API
     *
     * @param notifyId 읽음 처리할 알림 ID
     * @param userDetails 인증된 사용자 정보
     * @return 처리 성공 시 204 No Content
     */
    @Operation(summary = "단일 알림 읽음 처리", description = "선택한 알림을 읽음 처리합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<Void>> readNotification(
            @Parameter(description = "알림 ID", example = "1")
            @PathVariable(name = "id") final Long notifyId,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 모든 알림을 읽는 API
     *
     * @return 처리 성공 시 204 No Content
     */
    @Operation(summary = "모든 알림 읽음 처리", description = "회원이 보유한 모든 알림을 읽음 처리합니다.")
    @PatchMapping("/all/read")
    public ResponseEntity<CommonApiResponse<Void>> readAllNotification(){
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    /**
     * 읽지 않은 알림 목록 조회
     *
     * @param userDetails 인증된 사용자 정보
     * @return 읽지 않은 알림 목록
     */
    @Operation(summary = "읽지 않은 알림 목록 조회", description = "회원이 아직 읽지 않은 알림 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<NotifyResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        List<NotifyResponse> response = new ArrayList<>();
        response.add(NotifyResponse.builder()
                        .id(1L)
                        .name("아직 끝내지 않은 루틴이 있어요.힘내서 마무리 해볼까요?")
                        .type(NotificationType.ROUTINE)
                        .isRead(false)
                        .createdAt(LocalDate.now())
                .build());
        return ResponseEntity.ok(CommonApiResponse.success(response));
    }
}
