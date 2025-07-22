package com.honlife.core.app.controller.admin.item.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminItemListedRequest {
    // 관리자 지정 활성화 여부
    private Boolean isListed;
}
