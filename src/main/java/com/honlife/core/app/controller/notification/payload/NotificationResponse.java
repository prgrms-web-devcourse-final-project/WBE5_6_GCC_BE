package com.honlife.core.app.controller.notification.payload;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Setter
@Builder
@Data
public class NotificationResponse {

    private Long notificationId;
    private Boolean isEmail;
    private Boolean isRoutine;
    private Boolean isBadge;
}
