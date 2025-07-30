package com.honlife.core.app.controller.notification.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Setter
@Builder
@Data
public class NotificationPayload {

    @Schema(name = "isQuest", description ="퀘스트 알림 설정 값", example = "true")
    private Boolean isQuest;
    @Schema(name = "isRoutine", description ="루틴 알림 설정 값", example = "true")
    private Boolean isRoutine;
    @Schema(name = "isBadge", description ="업적 알림 설정 값", example = "true")
    private Boolean isBadge;
}
