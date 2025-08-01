package com.honlife.core.app.controller.member.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberItemEquippedRequest {
    private String oldItemKey;

    private String newItemKey;
}
