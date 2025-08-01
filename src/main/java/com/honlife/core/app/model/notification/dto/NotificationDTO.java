package com.honlife.core.app.model.notification.dto;

import com.honlife.core.app.model.notification.annotation.NotificationMemberUnique;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    private Long id;

    private Boolean isQuest;

    private Boolean isRoutine;

    private Boolean isBadge;

    @NotNull
    @NotificationMemberUnique
    private Long member;

}

