package com.honlife.core.app.controller.withdraw;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


@RestController
@RequestMapping(value = "/api/v1/withdrawReasons", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createWithdrawReason(
            @RequestBody @Valid final WithdrawReasonDTO withdrawReasonDTO) {
        final Long createdId = withdrawReasonService.create(withdrawReasonDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
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
