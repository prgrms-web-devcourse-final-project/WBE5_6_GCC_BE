package com.honlife.core.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberWeeklyQuestDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Integer progress;

    private Boolean idDone;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @NotNull
    private Long member;

    @NotNull
    private Long quest;

}
