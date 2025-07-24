package com.honlife.core.app.model.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import com.honlife.core.app.model.item.annotation.ItemItemKeyUnique;
import com.honlife.core.app.model.item.code.ItemType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @Size(max = 50)
    private String description;

    private Integer price;

    private ItemType type;

    @JsonProperty("isOwned")
    private Boolean isOwned;

    private Boolean isListed;

}
