package com.honlife.core.app.controller.item.playload;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BuyItemPayload {

    private Long memberId;
    private Long itemId;
    private LocalDateTime createdAt;

}
