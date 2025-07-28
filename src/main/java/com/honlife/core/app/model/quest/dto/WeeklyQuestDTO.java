package com.honlife.core.app.model.quest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.quest.code.QuestType;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeeklyQuestDTO {

  private Long id;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @JsonProperty("isActive")
  private Boolean isActive;

  @Size(max = 50)
  private String key;

  @Size(max = 255)
  private String name;

  private QuestType type;

  private Integer target;

  private Long category;

}