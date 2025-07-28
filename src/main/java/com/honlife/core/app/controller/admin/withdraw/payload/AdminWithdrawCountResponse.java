package com.honlife.core.app.controller.admin.withdraw.payload;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminWithdrawCountResponse {

    private WithdrawType withdrawType;
    private int withdrawCount;
}
