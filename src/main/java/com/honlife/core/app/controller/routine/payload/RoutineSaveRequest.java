package com.honlife.core.app.controller.routine.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.routine.code.RepeatType;

@Getter
@Setter
@Schema(description = "루틴 저장 요청")
public class RoutineSaveRequest {

    @NotNull(message = "카테고리는 필수입니다")
    @Schema(description = "카테고리 ID (대분류 선택시 대분류 ID, 소분류 선택시 소분류 ID)", example = "1", required = true)
    private Long categoryId;

    @NotBlank(message = "루틴 내용은 필수입니다")
    @Size(max = 255, message = "루틴 내용은 255자를 초과할 수 없습니다")
    @Schema(description = "루틴 내용", example = "아침 운동하기", required = true)
    private String content;

    @Schema(description = "수행 날짜 (RepeatType이 NONE일 때 사용)", example = "2025-01-15")
    private LocalDate targetDate;

    @Size(max = 255, message = "트리거 시간은 255자를 초과할 수 없습니다")
    @Schema(description = "트리거 시간대", example = "07:00")
    private String triggerTime;

    @Schema(description = "중요 루틴 여부", example = "true")
    private Boolean isImportant = false;

    @Schema(description = "반복 타입", example = "WEEKLY",
        allowableValues = {"DAILY", "WEEKLY", "MONTHLY", "CUSTOM", "NONE"})
    private RepeatType repeatType = RepeatType.DAILY;

    @Size(max = 100, message = "반복 값은 100자를 초과할 수 없습니다")
    @Schema(description = "반복 값 (예: WEEKLY의 경우 '1,3,5' = 월,수,금)", example = "1,3,5")
    private String repeatValue;
}