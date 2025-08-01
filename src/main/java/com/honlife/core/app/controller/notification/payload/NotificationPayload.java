package com.honlife.core.app.controller.notification.payload;

import com.honlife.core.app.model.notification.dto.NotificationDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Setter
@Builder
@Data
public class NotificationPayload {

    @NotNull
    private Boolean isQuest;
    @NotNull
    private Boolean isRoutine;
    @NotNull
    private Boolean isBadge;

    public static NotificationPayload fromDTO(NotificationDTO dto) {
        return NotificationPayload.builder()
            .isRoutine(dto.getIsRoutine())
            .isBadge(dto.getIsBadge())
            .isQuest(dto.getIsQuest())
            .build();
    }
}
