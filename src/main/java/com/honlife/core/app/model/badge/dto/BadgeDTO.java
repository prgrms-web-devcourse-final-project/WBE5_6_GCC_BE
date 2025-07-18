package com.honlife.core.app.model.badge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.badge.code.BadgeTier;


@Getter
@Setter
public class BadgeDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @Size(max = 50)
    private String key;

    @Size(max = 100)
    private String name;

    private BadgeTier tier;

    @Size(max = 100)
    private String how;

    private Integer requirement;

    @Size(max = 100)
    private String info;

    private Long category;

}
