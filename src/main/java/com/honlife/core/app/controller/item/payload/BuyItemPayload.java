package com.honlife.core.app.controller.item.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BuyItemPayload {
    private String memberId;
    private Long itemId;
    private LocalDateTime createdAt;
}
