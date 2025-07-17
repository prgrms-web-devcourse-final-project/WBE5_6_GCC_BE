package com.honlife.core.app.controller.notification.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Setter
@Builder
@Data
public class NotificationPayload {

    @NotNull
    private Boolean isEmail;
    @NotNull
    private Boolean isRoutine;
    @NotNull
    private Boolean isBadge;
}
