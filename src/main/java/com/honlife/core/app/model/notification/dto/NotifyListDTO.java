package com.honlife.core.app.model.notification.dto;

import com.honlife.core.app.model.notification.code.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotifyListDTO {
  private Long id;

  private NotificationType type;

  private String name;

  private LocalDateTime updateAt;

  private LocalDateTime createdAt;
}
