package com.honlife.core.app.model.withdraw.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.withdraw.code.WithdrawType;


@Getter
@Setter
public class WithdrawReasonDTO {

    private Long id;
    private WithdrawType type;
    private String reason;
    private LocalDateTime createdAt;

}
