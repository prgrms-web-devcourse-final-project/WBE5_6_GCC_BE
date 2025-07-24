package com.honlife.core.app.controller.admin.item.payload;

import com.honlife.core.app.model.item.code.ItemType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminItemRequest {

  @NotNull
  private ItemType itemType;
  @NotNull
  private String itemName;
  @NotNull
  private Integer price;

  private String itemDescription;

  private Boolean isListed;
}
