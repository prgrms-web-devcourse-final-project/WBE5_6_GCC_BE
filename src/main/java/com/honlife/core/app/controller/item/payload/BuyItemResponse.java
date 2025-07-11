package com.honlife.core.app.controller.item.payload;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BuyItemResponse {
    private String memberId;
    private Long itemId;
    private LocalDateTime createdAt;
}
