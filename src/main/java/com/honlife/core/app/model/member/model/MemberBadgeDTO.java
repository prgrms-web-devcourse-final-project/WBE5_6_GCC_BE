package com.honlife.core.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberBadgeDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Long member;

    @NotNull
    private Long badge;

}
