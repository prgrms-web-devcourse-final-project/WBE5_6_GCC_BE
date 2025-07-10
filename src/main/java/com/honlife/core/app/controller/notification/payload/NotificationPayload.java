package com.honlife.core.app.controller.notification.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationPayload {

    @Schema(
            description = "조작을 원하는 알람 타입입니다.",
            example = "EMAIL",
            allowableValues = { "EMAIL", "ROUTINE", "CHALLENGE" }
    )
    private String type;
    @Schema(
            description = "알림 ON/OFF 여부입니다.",
            example = "true"
    )
    private Boolean onOff;
}
