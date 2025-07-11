package com.honlife.core.app.model.item.dto;

import com.honlife.core.app.model.item.code.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ItemUpdateRequestDTO", description = "상점 아이템 수정 요청 DTO")
public class ItemUpdateRequestDTO {

  @NotNull
  @Schema(description = "아이템 ID", example = "1")
  private Long id;

  @Size(max = 50)
  @Schema(description = "아이템 키", example = "item_clean_001")
  private String itemKey;

  @Size(max = 50)
  @Schema(description = "아이템 이름", example = "청소 마법 지팡이")
  private String name;

  @Schema(description = "아이템 가격", example = "500")
  private Integer price;

  @Schema(description = "아이템 타입", example = "DECOR")
  private ItemType type;
}
