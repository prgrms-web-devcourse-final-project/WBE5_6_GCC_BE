package com.honlife.core.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honlife.core.app.model.auth.code.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.honlife.core.app.model.member.annotation.MemberEmailUnique;
import com.honlife.core.app.model.member.annotation.MemberNicknameUnique;
import com.honlife.core.app.model.member.code.ResidenceExperience;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    private Role role;

    @NotNull
    @Size(max = 50)
    @MemberEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 10)
    private String name;

    @NotNull
    @Size(max = 50)
    @MemberNicknameUnique
    private String nickname;

    private ResidenceExperience residenceExperience;

    @Size(max = 255)
    private String region1Dept;

    @Size(max = 255)
    private String region2Dept;

    @Size(max = 255)
    private String region3Dept;

    private Boolean isVerified;

}
