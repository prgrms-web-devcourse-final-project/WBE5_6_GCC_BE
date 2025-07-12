package com.honlife.core.app.controller.member.payload;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberWithdrawRequest {

    @NotBlank
    private WithdrawType withdrawType;
    private String etcReason;
}
