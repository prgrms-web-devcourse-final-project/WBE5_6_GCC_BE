package com.honlife.core.app.controller.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "루틴 저장 요청")
public class RoutineSaveRequest {

    @NotNull(message = "카테고리는 필수입니다")
    @Schema(description = "카테고리 ID (대분류 선택시 대분류 ID, 소분류 선택시 소분류 ID)", example = "1", required = true)
    private Long categoryId;


    @NotBlank(message = "루틴 이름은 필수입니다")
    @Schema(description = "루틴 이름", example = "변기 청소하기", required = true)
    private String name;

    @Schema(description = "대분류 카테고리", example = "청소")
    private String majorCategory;

    @Schema(description = "소분류 카테고리", example = "화장실 청소")
    private String subCategory;


    @NotNull(message = "반복 유형은 필수입니다")
    @Schema(description = "반복 유형", example = "WEEKLY")
    private RepeatType repeatType;


    @NotNull(message = "루틴 시작 날짜는 필수입니다")
    @Schema(description = "루틴 시작 날짜", example = "2025-07-01")
    private LocalDate startRoutineDate;


    @Size(max = 255, message = "트리거 시간은 255자를 초과할 수 없습니다")
    @Schema(description = "트리거 시간대", example = "07:00")
    private String triggerTime;

    @Schema(description = "중요 루틴 여부", example = "true")
    private Boolean isImportant = false;


    @Size(max = 100, message = "반복 값은 100자를 초과할 수 없습니다")
    @Schema(description = "반복 값 (예: WEEKLY의 경우 '1,3,5' = 월,수,금)", example = "1,3,5")
    private String repeatValue;

    @Schema(description = "주 반복 간격 (1 = 매주, 2 = 격주 등)", example = "1")
    private Integer repeatInterval = 1;  // 기본값: 매주



}