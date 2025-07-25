package com.honlife.core.app.controller.admin.withdraw.payload;

import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
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

    public static AdminWithdrawResponse fromDTO(WithdrawReasonDTO withdrawReasonDTO){
        return AdminWithdrawResponse.builder()
            .reason(withdrawReasonDTO.getReason())
            .createTime(withdrawReasonDTO.getCreatedAt())
            .build();
    }



}