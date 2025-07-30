package com.honlife.core.app.model.notification.dto;

import com.honlife.core.app.model.notification.code.NotificationType;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyListDTO {

    private Long id;

    private String name;

    private NotificationType type;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
