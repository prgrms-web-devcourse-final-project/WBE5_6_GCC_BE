package com.honlife.core.app.controller.withdraw.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.honlife.core.app.model.withdraw.code.WithdrawType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 응답에서 제외
public class WithdrawReasonRequest {

    @Schema(description = "탈퇴 사유 타입 (ENUM)", example = "ETC")
    private WithdrawType type;

    @Schema(description = "직접 입력한 탈퇴 사유 (ETC일 경우만)", example = "앱 사용이 너무 어려워요")
    private String reason;

    @Schema(
            description = "모든 정보 삭제에 대한 사용자 동의 여부 (필수)",
            example = "true",
            required = true
    )
    @NotNull(message = "정보 삭제 동의는 필수입니다.")
    private Boolean agreedToDelete;
}
