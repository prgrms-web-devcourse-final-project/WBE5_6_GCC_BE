package com.honlife.core.app.controller.admin.routine.payload;

import com.honlife.core.app.model.routine.code.RepeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "추천 루틴 프리셋 상세 조회 응답")
public class AdminRoutinePresetDetailResponse {

    @Schema(description = "프리셋 ID", example = "1")
    private Long presetId;

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "대분류 카테고리", example = "청소")
    private String majorCategory;

    @Schema(description = "카테고리 이름", example = "청소")
    private String categoryName;

    @Schema(description = "루틴 내용", example = "아침 스트레칭 하기")
    private String content;

    @Schema(description = "소분류 카테고리", example = "화장실 청소")
    private String subCategory;

    @Schema(description = "루틴 이름", example = "변기 청소하기")
    private String name;

    @Schema(description = "트리거 시간", example = "09:00")
    private String triggerTime;

    @Schema(description = "중요 루틴 여부", example = "false")
    private Boolean isImportant;

    @Schema(description = "반복 타입", example = "WEEKLY")
    private RepeatType repeatType;

    @Schema(description = "반복 값", example = "1,3,5")
    private String repeatValue;

    @Schema(description = "루틴 시작 날짜", example = "2025-11-10")
    private LocalDate startRoutineDate;

    @Schema(description = "활성화 여부", example = "true")
    private Boolean isActive;

    @Schema(description = "활성화 여부", example = "true")
    private Boolean isDone;

    @Schema(description = "생성일시", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2025-01-15T14:20:00")
    private LocalDateTime updatedAt;

}