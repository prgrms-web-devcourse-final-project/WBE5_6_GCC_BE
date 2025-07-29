package com.honlife.core.app.model.withdraw.dto;

import com.honlife.core.app.model.withdraw.code.WithdrawType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WithdrawCountDTO {
    private WithdrawType withdrawType;
    private Long withdrawCount;
}
