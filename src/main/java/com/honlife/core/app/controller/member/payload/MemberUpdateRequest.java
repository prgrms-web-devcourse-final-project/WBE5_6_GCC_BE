package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.member.code.ResidenceExperience;
import com.honlife.core.app.model.member.model.MemberDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberUpdateRequest {
    @NotBlank(message = "nickname must be not blank")
    private String nickname;
    private ResidenceExperience residenceExperience;
    private String regionDept1;
    private String regionDept2;
    private String regionDept3;

    //TODO: 개발시 활용
    public static MemberUpdateRequest fromDTO(MemberDTO memberDTO) {
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest();
        memberUpdateRequest.nickname = memberDTO.getNickname();
        memberUpdateRequest.residenceExperience = memberDTO.getResidenceExperience();
        memberUpdateRequest.regionDept1 = memberDTO.getRegion1Dept();
        memberUpdateRequest.regionDept2 = memberDTO.getRegion2Dept();
        memberUpdateRequest.regionDept3 = memberDTO.getRegion3Dept();
        return memberUpdateRequest;
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
            .nickname(this.nickname)
            .residenceExperience(this.residenceExperience)
            .region1Dept(this.regionDept1)
            .region2Dept(this.regionDept2)
            .region3Dept(this.regionDept3)
            .build();
    }
}
