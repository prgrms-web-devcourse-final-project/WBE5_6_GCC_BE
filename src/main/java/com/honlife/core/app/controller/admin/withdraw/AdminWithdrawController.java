package com.honlife.core.app.controller.admin.withdraw;

import com.honlife.core.app.controller.admin.withdraw.payload.AdminWithdrawResponse;
import com.honlife.core.app.model.withdraw.dto.WithdrawReasonDTO;
import com.honlife.core.app.model.withdraw.service.WithdrawReasonService;
import com.honlife.core.infra.payload.PageParam;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.PageResponse;
import com.honlife.core.infra.response.ResponseCode;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/admin/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
public class AdminWithdrawController {


    private final WithdrawReasonService withdrawReasonService;


    /**
     * 회원 탈퇴 사유를 조회하는 API입니다.
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return 모든 탈퇴 사유에 대한 리스트를 반환합니다. 시작일과 종료일이 함께 넘어온 경우, 두 날짜 사이의 탈퇴 사유만 반환합니다.
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<PageResponse<AdminWithdrawResponse>>> getAllWithdrawReason(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,
            @Valid @RequestBody PageParam pageParam
    ) {

            Pageable pageable = PageRequest.of(pageParam.getPage()-1, pageParam.getSize());
            Page<WithdrawReasonDTO> page =  withdrawReasonService.findPagedByDate(pageable, startDate, endDate);

            if(pageParam.getPage() != 1 && page.getContent().isEmpty()){
                return ResponseEntity.badRequest().body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
            }

            Page<AdminWithdrawResponse> responsePage = page.map(
                AdminWithdrawResponse::fromDTO);

            PageResponse<AdminWithdrawResponse> response = new PageResponse<>(responsePage, 5);

            return ResponseEntity.ok(CommonApiResponse.success(response));
    }

}
