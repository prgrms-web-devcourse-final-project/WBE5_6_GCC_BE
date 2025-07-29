package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.controller.member.payload.MemberPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponseWrapper {

    private MemberPayload member;

    private MemberBadgeResponse equippedBadge;

}
