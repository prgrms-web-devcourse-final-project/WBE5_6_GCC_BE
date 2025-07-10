package com.honlife.core.app.controller.member.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberPointPayload {

    private String email;

    private Long currentPoint;
}
