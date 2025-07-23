package com.honlife.core.app.controller.member.wrapper;

import com.honlife.core.app.controller.member.payload.MemberBadgeResponse;
import com.honlife.core.app.controller.member.payload.MemberPayload;
import lombok.Data;

@Data
public class MemberResponseWrapper {

    private MemberPayload member;

    private MemberBadgeResponse badge;

    public MemberResponseWrapper(MemberPayload member, MemberBadgeResponse equippedBadge) {
        this.member = member;
        this.badge = equippedBadge;
    }



}
