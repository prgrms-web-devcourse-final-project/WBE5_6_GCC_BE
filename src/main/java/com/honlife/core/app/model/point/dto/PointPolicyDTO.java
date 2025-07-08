package com.honlife.core.app.model.point.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.point.code.PointSourceType;


@Getter
@Setter
public class PointPolicyDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private PointSourceType type;

    @Size(max = 50)
    private String referenceKey;

    private Integer point;

}
