package com.honlife.core.app.controller.notification.payload;

import com.honlife.core.app.model.notification.code.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyResponse {

    @Schema(description = "알림 ID", example = "1")
    private Long id;

    @Schema(description = "알림 내용", example = "아직 끝내지 않은 루틴이 있어요. 힘내서 마무리 해볼까요?")
    private String name;

    @Schema(description = "알림 종류 (ROUTINE, QUEST, BADGE 중 하나)", example = "ROUTINE")
    private NotificationType type;

    @Schema(description = "알림 읽음 여부", example = "false")
    private Boolean isRead;

    @Schema(description = "알림 생성일", example = "2025-07-29")
    private LocalDate createdAt;

}
