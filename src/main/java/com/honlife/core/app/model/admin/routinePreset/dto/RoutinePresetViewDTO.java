package com.honlife.core.app.model.admin.routinePreset.dto;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoutinePresetViewDTO {

  private Long presetId;

  private Long categoryId;

  private String categoryName;

  private String content;

  private Boolean isActive;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
