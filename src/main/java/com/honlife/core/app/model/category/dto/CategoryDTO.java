package com.honlife.core.app.model.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.category.code.CategoryType;


@Getter
@Setter
public class CategoryDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Long parent;

    @Size(max = 25)
    private String name;

    private CategoryType type;

    @NotNull
    private Long member;

    private String emoji;

}
