package com.honlife.core.app.controller.item.playload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(
        name    = "ItemPayload",
        example = """
    {
      "item_id": 1,
      "type": "모자",
      "item_key": "head_item_01",
      "item_Name": "청소모자",
      "item_point: 100,
    }
  """
)
public class ItemPayload {

    private Long itemId;

    private String type;

    private String itemKey;

    private String itemName;

    private Integer itemPoint;



}
