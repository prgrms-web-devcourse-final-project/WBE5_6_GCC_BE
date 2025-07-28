package com.honlife.core.app.model.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.category.domain.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.honlife.core.app.model.category.code.CategoryType;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Long parent;

    private List<ChildCategoryDTO> children = new ArrayList<>();

    @Size(max = 25)
    private String name;

    private CategoryType type;

    @NotNull
    private Long member;

    private String emoji;

}
