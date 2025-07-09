package com.honlife.core.app.controller.item.playload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(
        name = "BuyItemPayload",
        example = """
                  {
                    "memberId": 123,
                    "itemId": 1,
                    "createdAt": "2025-07-09T10:30:00"
                  }
                """
)
public class BuyItemPayload {

    private Long memberId;
    private Long itemId;
    private LocalDateTime createdAt;

}
