package com.honlife.core.app.controller.admin;

import com.honlife.core.app.controller.admin.payload.LoginLogRequest;
import com.honlife.core.app.controller.admin.payload.LoginLogResponse;
import com.honlife.core.app.controller.admin.payload.PointLogRequest;
import com.honlife.core.app.controller.admin.payload.PointLogResponse;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "로그", description = "로그 관련 API들 입니다.")
@RequestMapping(value = "/api/v1/admin/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminLogController {

    /**
     * 지정한 기간 내 유저 로그인 로그를 조회합니다.
     * ※ 추후 pagination(페이지네이션)이 적용될 예정입니다.
     * * 현재는 전체 데이터를 반환하지만, 향후 페이지 단위로 분리될 수 있습니다.
     *
     * @param loginLogRequest 로그 조회 시 필요 정보(필수 아님)
     * @return List<LoginLogResponse>
     */
    @Operation(
            summary = "유저 접속 기록 조회",
            description = " 지정한 시작일(`startDate`)과 종료일(`endDate`) 사이의 로그인 기록을 조회합니다. <br>" +
                    "- 날짜는 yyyy-MM-dd'T'HH:mm:ss 형식으로 전달해야 합니다. <br>" +
                    "- 추후 pagination(페이지네이션)이 적용될 예정입니다.")
    @GetMapping("/login")
    public ResponseEntity<CommonApiResponse<List<LoginLogResponse>>> getLoginLogsByDate(
            @RequestBody LoginLogRequest loginLogRequest
    ) {
        List<LoginLogResponse> logs = new ArrayList<>();
        logs.add(LoginLogResponse.builder()
                .loginLogId(1L)
                .memberId(1L)
                .time(LocalDateTime.of(2025, 7, 2, 10, 30))
                .build());
        logs.add(LoginLogResponse.builder()
                .loginLogId(2L)
                .memberId(2L)
                .time(LocalDateTime.of(2025, 7, 10, 12, 30))
                .build());
        return ResponseEntity.ok(CommonApiResponse.success(logs));
    }

    /**
     * 지정한 기간 내 포인트 로그를 조회합니다.
     * ※ 추후 pagination(페이지네이션)이 적용될 예정입니다.
     * * 현재는 전체 데이터를 반환하지만, 향후 페이지 단위로 분리될 수 있습니다.
     *
     * @param pointLogRequest 로그 조회 시 필요 정보(필수 아님)
     * @return List<PointLogResponse>
     */
    @Operation(
            summary = "포인트 기록 조회",
            description = " 지정한 시작일(`startDate`)과 종료일(`endDate`) 포인트 지급 및 소비 기록을 조회합니다. <br>" +
                    "- 날짜는 yyyy-MM-dd'T'HH:mm:ss 형식으로 전달해야 합니다. <br>" +
                    "- 추후 pagination(페이지네이션)이 적용될 예정입니다."
    )
    @GetMapping("/point")
    public ResponseEntity<CommonApiResponse<List<PointLogResponse>>> getPointLogs(
            @RequestBody PointLogRequest pointLogRequest
    ) {

        List<PointLogResponse> logs = new ArrayList<>();
        if (pointLogRequest.getPointLogType() == PointLogType.GET) {
            logs.add(PointLogResponse.builder()
                    .pointLogId(1L)
                    .memberId(1L)
                    .pointLogType(PointLogType.GET)
                    .point(100)
                    .reason(PointSourceType.CHALLENGE)
                    .time(LocalDateTime.of(2025, 7, 2, 10, 30))
                    .build());
            logs.add(PointLogResponse.builder()
                    .pointLogId(2L)
                    .memberId(2L)
                    .pointLogType(PointLogType.GET)
                    .point(100)
                    .reason(PointSourceType.WEEKLY)
                    .time(LocalDateTime.of(2025, 7, 10, 12, 30))
                    .build());
        } else if (pointLogRequest.getPointLogType() == PointLogType.USE) {
            logs.add(PointLogResponse.builder()
                    .pointLogId(3L)
                    .memberId(4L)
                    .pointLogType(PointLogType.USE)
                    .point(100)
                    .reason(PointSourceType.CHALLENGE)
                    .time(LocalDateTime.of(2025, 7, 5, 11, 30))
                    .build());
            logs.add(PointLogResponse.builder()
                    .pointLogId(4L)
                    .memberId(5L)
                    .pointLogType(PointLogType.USE)
                    .point(100)
                    .reason(PointSourceType.WEEKLY)
                    .time(LocalDateTime.of(2025, 7, 9, 12, 30))
                    .build());
        } else {
            logs.add(PointLogResponse.builder()
                    .pointLogId(5L)
                    .memberId(6L)
                    .pointLogType(PointLogType.GET)
                    .point(100)
                    .reason(PointSourceType.CHALLENGE)
                    .time(LocalDateTime.of(2025, 7, 2, 10, 30))
                    .build());
            logs.add(PointLogResponse.builder()
                    .pointLogId(6L)
                    .memberId(5L)
                    .pointLogType(PointLogType.USE)
                    .point(100)
                    .reason(PointSourceType.WEEKLY)
                    .time(LocalDateTime.of(2025, 7, 9, 12, 30))
                    .build());
        }
        return ResponseEntity.ok(CommonApiResponse.success(logs));
    }
}