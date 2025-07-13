package com.honlife.core.app.model.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventQuestDTO {

  private Long id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @JsonProperty("isActive")
  private Boolean isActive;

  @Size(max = 50)
  private String key;

  @Size(max = 255)
  private String name;

  private String info;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

}