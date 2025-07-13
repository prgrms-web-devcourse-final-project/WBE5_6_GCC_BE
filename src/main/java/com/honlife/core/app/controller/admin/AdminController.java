package com.honlife.core.app.controller.admin;

import com.honlife.core.app.model.loginLog.dto.LoginLogResponseDTO;
import com.honlife.core.app.controller.admin.payload.PointLogResponse;
import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="관리자 로그", description = "관리자 로그 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin/log", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {



  @Operation(
      summary = "유저 접속 기록 조회",
      description = "유저의 로그인 기록을 반환합니다."
  )
  @GetMapping("/login")
  public ResponseEntity<CommonApiResponse<List<LoginLogResponseDTO>>> getLoginLogsByMember(Authentication authentication) {
    List<LoginLogResponseDTO> logs = List.of(
        new LoginLogResponseDTO(1L, "aakjdf@naver.com", LocalDateTime.parse("2025-07-06T09:00:00")),
        new LoginLogResponseDTO(2L, "aakjdf@naver.com", LocalDateTime.parse("2025-07-07T08:45:00"))
    );

    return ResponseEntity.ok(CommonApiResponse.success(logs));
  }

  @Operation(
      summary = "포인트 기록 조회",
      description = "포인트 지급 및 소비 기록을 반환합니다."
  )
  @GetMapping("/point")
  public ResponseEntity<CommonApiResponse<List<PointLogResponse>>> getPointLogs(Authentication authentication) {
    List<PointLogResponse> logs = List.of(
        new PointLogResponse(10L, "aakjdf@naver.com", PointLogType.GET, 1000, "퀘스트 완료", LocalDateTime.parse("2025-07-05T14:00:00")),
        new PointLogResponse(11L, "aakjdf@naver.com", PointLogType.USE, 500, "아이템 구매", LocalDateTime.parse("2025-07-05T15:00:00"))
    );

    return ResponseEntity.ok(CommonApiResponse.success(logs));
  }

  @Operation(
      summary = "포인트 로그 삭제",
      description = "포인트 지급 및 소비 기록을 삭제합니다.",
      parameters = {
          @Parameter(
              name = "logId",
              description = "삭제할 포인트 로그 ID",
              required = true,
              example = "42"
          )
      }
  )
  @DeleteMapping("/point/{logId}")
  public ResponseEntity<CommonApiResponse<String>> pointLogsDelete(@PathVariable Long logId, Authentication authentication) {
    if (logId == 42L) {
      return ResponseEntity.ok(CommonApiResponse.success("포인트 로그 삭제 완료"));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_FOUND_LOG.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_LOG));
    }
  }






}
