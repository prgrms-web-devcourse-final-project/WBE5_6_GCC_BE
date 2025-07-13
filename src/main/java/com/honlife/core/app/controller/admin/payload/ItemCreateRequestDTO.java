package com.honlife.core.app.controller.admin.payload;

import com.honlife.core.app.model.item.code.ItemType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ItemCreateRequestDTO", description = "상점 아이템 추가 요청 DTO")
public class ItemCreateRequestDTO {

  @NotNull
  @Size(max = 50)
  @Schema(description = "아이템 키 (고유값)", example = "item_clean_001")
  private String itemKey;

  @NotNull
  @Size(max = 50)
  @Schema(description = "아이템 이름", example = "깔끔 청소 도우미")
  private String name;

  @NotNull
  @Schema(description = "아이템 가격", example = "300")
  private Integer price;

  @NotNull
  @Schema(description = "아이템 타입", example = "CONSUMABLE")
  private ItemType type;
}
