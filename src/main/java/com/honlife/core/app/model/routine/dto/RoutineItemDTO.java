package com.honlife.core.app.model.routine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RoutineItemDTO {

    @Schema(description = "스케줄 ID", example = "1")
    private Long scheduleId;

    @Schema(description = "루틴 ID", example = "1")
    private Long routineId;

    @Schema(description = "대분류 카테고리", example = "청소")
    private String majorCategory;

    @Schema(description = "소분류 카테고리", example = "화장실 청소")
    private String subCategory;

    @Schema(description = "루틴 이름", example = "변기 청소하기")
    private String name;

    @Schema(description = "트리거 시간", example = "09:00")
    private String triggerTime;

    @Schema(description = "완료 여부", example = "true")
    private Boolean isDone;

    @Schema(description = "중요 루틴 여부", example = "false")
    private Boolean isImportant;

    private LocalDate date;
  }



