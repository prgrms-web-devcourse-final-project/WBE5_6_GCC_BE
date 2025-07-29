package com.honlife.core.app.controller.notification.payload;

import com.honlife.core.app.model.notification.code.NotificationType;
import com.honlife.core.app.model.notification.domain.NotifyList;
import com.honlife.core.app.model.notification.dto.NotifyListDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotifyListResponse {
  private Long id;

  private NotificationType type;

  private String name;

  private LocalDateTime updateAt;

  private LocalDateTime createdAt;

  public static NotifyListResponse fromDto(NotifyListDTO dto) {
    return NotifyListResponse.builder()
        .id(dto.getId())
        .type(dto.getType())
        .name(dto.getName())
        .updateAt(dto.getUpdateAt())
        .createdAt(dto.getCreatedAt())
        .build();
  }

}
