package com.honlife.core.app.model.notification.dto;

import com.honlife.core.app.model.notification.annotation.NotificationMemberUnique;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Long id;

    private Boolean email;

    private Boolean routine;

    private Boolean challenge;

    @NotNull
    @NotificationMemberUnique
    private Long member;

}

