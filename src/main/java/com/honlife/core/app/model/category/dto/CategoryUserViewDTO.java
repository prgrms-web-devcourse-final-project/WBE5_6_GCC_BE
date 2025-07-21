package com.honlife.core.app.model.category.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.category.code.CategoryType;
import com.honlife.core.app.model.category.domain.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryUserViewDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Long parentId;

    private String parentName;

    @Size(max = 25)
    private String name;

    private CategoryType type;

    @NotNull
    private Long member;

    private String emoji;

    /**
     * 엔티티에서 DTO로 변환
     * @param category
     * @return CategoryDTOCustom
     */
    public static CategoryUserViewDTO fromEntity(Category category) {
        return CategoryUserViewDTO.builder()
            .id(category.getId())
            .name(category.getName())
            .type(category.getType())
            .createdAt(category.getCreatedAt())
            .updatedAt(category.getUpdatedAt())
            .isActive(category.getIsActive())
            .parentId(category.getParent()==null? null : category.getParent().getId())
            .parentName(category.getParent()==null? null : category.getParent().getName())
            .member(category.getMember().getId())
            .emoji(category.getEmoji())
            .build();
    }

}
