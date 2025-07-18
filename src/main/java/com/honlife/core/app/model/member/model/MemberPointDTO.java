package com.honlife.core.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.member.annotation.MemberPointMemberUnique;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberPointDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private Integer point;

    @NotNull
    @MemberPointMemberUnique
    private Long member;

}
