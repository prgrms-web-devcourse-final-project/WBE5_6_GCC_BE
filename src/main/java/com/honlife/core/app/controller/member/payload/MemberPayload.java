package com.honlife.core.app.controller.member.payload;

import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.member.code.ResidenceExperience;
import com.honlife.core.app.model.member.model.MemberDTO;

@Getter
@Setter
public class MemberPayload {

    private String email;
    private String name;
    private String nickname;
    private ResidenceExperience residenceExperience;
    private String regionDept1;
    private String regionDept2;
    private String regionDept3;

    //TODO: 개발시 활용
    public static MemberPayload fromDTO(MemberDTO memberDTO) {
        MemberPayload memberPayload = new MemberPayload();
        memberPayload.email = memberDTO.getEmail();
        memberPayload.nickname = memberDTO.getNickname();
        memberPayload.residenceExperience = memberDTO.getResidenceExperience();
        memberPayload.regionDept1 = memberDTO.getRegion1Dept();
        memberPayload.regionDept2 = memberDTO.getRegion2Dept();
        memberPayload.regionDept3 = memberDTO.getRegion3Dept();
        return memberPayload;
    }
}
