package com.honlife.core.app.model.badge.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.badge.code.CountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeProgressDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Long member;

    @NotNull
    @Size(max = 20)
    private String progressType;

    @NotNull
    @Size(max = 20)
    private String progressKey;

    @NotNull
    private CountType countType;

    @NotNull
    private Integer count;

    private LocalDate lastDate;
}
