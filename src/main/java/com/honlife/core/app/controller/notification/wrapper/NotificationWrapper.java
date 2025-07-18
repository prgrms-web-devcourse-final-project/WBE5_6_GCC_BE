package com.honlife.core.app.controller.notification.wrapper;

import com.honlife.core.app.controller.notification.payload.NotificationPayload;
import lombok.Getter;

@Getter
public class NotificationWrapper {

    private final NotificationPayload notification;

    public NotificationWrapper(NotificationPayload notification) {
        this.notification = notification;
    }
}
