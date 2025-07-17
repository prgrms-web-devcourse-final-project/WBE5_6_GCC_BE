package com.honlife.core.app.controller.notification.payload;

import com.honlife.core.app.model.notification.dto.NotificationDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    public static NotificationPayload fromDTO(NotificationDTO dto) {
        return NotificationPayload.builder()
            .isRoutine(dto.getIsRoutine())
            .isBadge(dto.getIsBadge())
            .isEmail(dto.getIsEmail())
            .build();
    }
}
