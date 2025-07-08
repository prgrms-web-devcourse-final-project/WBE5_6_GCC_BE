package spring.grepp.honlife.app.controller.member.payload;

import lombok.Getter;
import lombok.Setter;
import spring.grepp.honlife.app.model.member.code.ResidenceExperience;
import spring.grepp.honlife.app.model.member.model.MemberDTO;

@Getter
@Setter
public class MemberPayload {

    private Long userId;
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
        memberPayload.userId = memberDTO.getId();
        memberPayload.email = memberDTO.getEmail();
        memberPayload.nickname = memberDTO.getNickname();
        memberPayload.residenceExperience = memberDTO.getResidenceExperience();
        memberPayload.regionDept1 = memberDTO.getRegionDept1();
        memberPayload.regionDept2 = memberDTO.getRegionDept2();
        memberPayload.regionDept3 = memberDTO.getRegionDept3();
        return memberPayload;
    }
}
