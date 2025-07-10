package com.honlife.core.app.controller.notification.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationPayload {
    private String type;
    private Boolean onOff;
}
