package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MemberWithdrawRequest {

    @NotNull
    private WithdrawType withdrawType;
    private String etcReason;
}
