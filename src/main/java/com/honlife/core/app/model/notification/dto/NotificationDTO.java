package com.honlife.core.app.model.notification.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Long id;
    private Boolean email;
    private Boolean routine;
    private Boolean challenge;

}
