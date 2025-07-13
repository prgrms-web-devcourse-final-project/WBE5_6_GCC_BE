package com.honlife.core.app.controller.admin.withdraw.payload;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminWithdrawResponse {

    private String reason;
    private LocalDateTime createTime;

}