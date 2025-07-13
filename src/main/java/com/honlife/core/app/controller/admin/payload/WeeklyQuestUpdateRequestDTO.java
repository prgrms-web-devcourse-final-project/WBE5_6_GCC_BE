package com.honlife.core.app.controller.admin.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Text;

@Getter
@Setter
@Schema(name = "WeeklyQuestUpdateDTO", description = "주간 퀘스트 수정 요청 DTO")
public class WeeklyQuestUpdateRequestDTO {

  @NotNull
  @Schema(description = "퀘스트 ID", example = "1")
  private Long questId;

  @Size(max = 255)
  @Schema(description = "퀘스트 이름", example = "청소 루틴 3번 완료하기 (수정)")
  private String name;

  @Schema(description = "퀘스트 설명", example = "수정된 퀘스트 설명입니다.")
  private Text info;
}
