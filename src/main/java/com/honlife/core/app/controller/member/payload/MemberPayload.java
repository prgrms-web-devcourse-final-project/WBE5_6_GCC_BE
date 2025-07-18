package com.honlife.core.app.controller.member.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.member.code.ResidenceExperience;
import com.honlife.core.app.model.member.model.MemberDTO;

@Getter
@Setter
public class MemberPayload {

    @NotBlank(message = "name must be not blank")
    private String name;
    @NotBlank(message = "nickname must be not blank")
    private String nickname;
    private ResidenceExperience residenceExperience;
    private String regionDept1;
    private String regionDept2;
    private String regionDept3;

    //TODO: 개발시 활용
    public static MemberPayload fromDTO(MemberDTO memberDTO) {
        MemberPayload memberPayload = new MemberPayload();
        memberPayload.name = memberDTO.getName();
        memberPayload.nickname = memberDTO.getNickname();
        memberPayload.residenceExperience = memberDTO.getResidenceExperience();
        memberPayload.regionDept1 = memberDTO.getRegion1Dept();
        memberPayload.regionDept2 = memberDTO.getRegion2Dept();
        memberPayload.regionDept3 = memberDTO.getRegion3Dept();
        return memberPayload;
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
            .name(this.name)
            .nickname(this.nickname)
            .residenceExperience(this.residenceExperience)
            .region1Dept(this.regionDept1)
            .region2Dept(this.regionDept2)
            .region3Dept(this.regionDept3)
            .build();
    }
}
