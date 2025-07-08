package com.honlife.core.app.model.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.item.annotation.ItemItemKeyUnique;
import com.honlife.core.app.model.item.code.ItemType;


@Getter
@Setter
public class ItemDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @Size(max = 50)
    @ItemItemKeyUnique
    private String itemKey;

    @Size(max = 50)
    private String name;

    private Integer price;

    private ItemType type;

}
