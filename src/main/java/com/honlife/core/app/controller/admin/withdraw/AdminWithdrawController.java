package com.honlife.core.app.controller.admin.withdraw;

import com.honlife.core.app.controller.admin.withdraw.payload.AdminWithdrawResponse;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="관리자 - 회원 탈퇴 사유", description = "관리자용 회원 탈퇴 사유 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWithdrawController {


    /**
     * 회원 탈퇴 사유를 조회하는 API입니다.
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return 모든 탈퇴 사유에 대한 리스트를 반환합니다. 시작일과 종료일이 함께 넘어온 경우, 두 날짜 사이의 탈퇴 사유만 반환합니다.
     */
    @Operation(
        summary = "회원 탈퇴 사유 조회",
        description = "회원 탈퇴 사유를 전체 조회합니다."
            + "지정한 시작일(`startDate`)과 종료일(`endDate`) 입력 시 두 날짜 사이의 탈퇴 사유를 조회합니다. <br>" +
            "- 날짜는 yyyy-MM-dd'T'HH:mm:ss 형식으로 전달해야 합니다. <br>" +
            "- 추후 pagination(페이지네이션)이 적용될 예정입니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<AdminWithdrawResponse>>> getAllWithdrawReason(
            @Parameter(description = "조회 시작일 (yyyy-MM-dd'T'HH:mm:ss)", example = "2025-07-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @Parameter(description = "조회 종료일 (yyyy-MM-dd'T'HH:mm:ss)", example = "2025-07-14T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate
    ) {
            List<AdminWithdrawResponse> response = new ArrayList<>();

            response.add(AdminWithdrawResponse.builder()
                .reason("이 서비스 너무 쓰레기 같고 재미 없어요 그만 쓸래요")
                .createTime(LocalDateTime.now())
                .build());
            response.add(AdminWithdrawResponse.builder()
                .reason("전 자유로운 영혼이 될 겁니다. 아무도 날 구속할 수 없어")
                .createTime(LocalDateTime.now())
                .build());

            return ResponseEntity.ok(CommonApiResponse.success(response));
    }

}
