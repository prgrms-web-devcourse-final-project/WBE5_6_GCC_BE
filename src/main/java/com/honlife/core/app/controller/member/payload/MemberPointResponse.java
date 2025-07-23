package com.honlife.core.app.controller.member.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberPointResponse {

    @Schema(name = "points", description = "회원이 보유한 포인트", example = "500")
    private Integer points;
}
