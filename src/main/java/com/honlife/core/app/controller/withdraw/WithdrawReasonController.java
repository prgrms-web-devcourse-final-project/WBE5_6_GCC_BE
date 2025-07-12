package com.honlife.core.app.controller.withdraw;

import com.honlife.core.app.controller.withdraw.payload.WithdrawReasonRequest;
import com.honlife.core.app.model.withdraw.code.WithdrawType;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
import com.honlife.core.app.model.withdraw.service.WithdrawReasonService;

@Tag(name = "탈퇴 사유",description = "탈퇴 사유 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/withdrawReasons", produces = MediaType.APPLICATION_JSON_VALUE)
public class WithdrawReasonController {

    private final WithdrawReasonService withdrawReasonService;

    public WithdrawReasonController(final WithdrawReasonService withdrawReasonService) {
        this.withdrawReasonService = withdrawReasonService;
    }

    @GetMapping
    public ResponseEntity<List<WithdrawReasonDTO>> getAllWithdrawReasons() {
        return ResponseEntity.ok(withdrawReasonService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WithdrawReasonDTO> getWithdrawReason(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(withdrawReasonService.get(id));
    }

    @Operation(
            summary = "탈퇴 사유 등록",
            description = """
    enum 값을 통해 탈퇴 사유를 등록합니다.
    - type이 'ETC'인 경우: 사용자가 입력한 reason이 저장됩니다.  
    - 그 외의 경우: 선택한 사유(enum의 label)가 서버에서 자동으로 reason으로 저장됩니다.
    """
    )
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createWithdrawReason(
            @Parameter(description = "탈퇴 사유 등록 요청 객체")
            @RequestBody WithdrawReasonRequest request
    ){
        String reasonToSave;

        if (request.getType() == WithdrawType.ETC) {
            if (request.getReason() == null || request.getReason().isBlank()) {
                throw new IllegalArgumentException("직접 입력 시 사유는 필수입니다.");
            }
            reasonToSave = request.getReason();
        } else {
            reasonToSave = request.getType().getLabel(); // enum의 한글 설명 사용
        }
        return ResponseEntity.ok(CommonApiResponse.noContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateWithdrawReason(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final WithdrawReasonDTO withdrawReasonDTO) {
        withdrawReasonService.update(id, withdrawReasonDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteWithdrawReason(@PathVariable(name = "id") final Long id) {
        withdrawReasonService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
