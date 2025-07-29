package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "루틴 아이템")
public class RoutineItemDTO {


  @Schema(description = "스케줄 ID", example = "3")
  private Long scheduleId;

  @Schema(description = "카테고리 ID", example = "3")
  private Long categoryId;

  @Schema(description = "루틴 ID", example = "3")
  private Long routineId;

  @Schema(description = "상위 카테고리", example = "건강")
  private String majorCategory;

  @Schema(description = "하위 카테고리", example = "운동")
  private String subCategory;

  @Schema(description = "루틴 이름", example = "저녁 조깅")
  private String name;

  @Schema(description = "루틴 실행 시간", example = "19:00")
  private String triggerTime;

  @Schema(description = "완료 여부", example = "false")
  private Boolean isDone;

  @Schema(description = "중요 여부", example = "true")
  private Boolean isImportant;

  @Schema(description = "해당 날짜", example = "2025-07-12")
  private LocalDate date;

  @Schema(description = "반복 타입", example = "WEEKLY")
  private RepeatType repeatType;

  @Schema(description = "반복 값", example = "1,3,5")
  private String repeatValue;

  @Schema(description = "루틴 시작 날짜", example = "2025-11-12")
  private String initDate;

  @Schema(description = "카테고리 이모지")
  private String emoji;

}
