package com.honlife.core.app.controller.member.admin;

import com.honlife.core.app.model.badge.dto.BadgeDTO;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="관리자 뱃지 관리", description = "관리자 뱃지 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminBadgeController {

  @Operation(
      summary = "뱃지 등록",
      description = "새로운 뱃지를 등록합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = BadgeDTO.class)
          )
      )
  )
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommonApiResponse.class)))
  @PostMapping("/badge")
  public ResponseEntity<?> registerBadge(@RequestBody BadgeDTO request, Authentication authentication) {
    if (request.getKey() == null || request.getName() == null) {
      return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
    return ResponseEntity.ok(CommonApiResponse.success("뱃지 등록 완료"));
  }

  // ✅ 2. 뱃지 수정
  @Operation(
      summary = "뱃지 수정",
      description = "기존 뱃지 정보를 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = BadgeDTO.class)
          )
      )
  )
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommonApiResponse.class)))
  @PatchMapping("/badge/{badgeId}")
  public ResponseEntity<?> updateBadge(@PathVariable Long badgeId, @RequestBody BadgeDTO request, Authentication authentication) {
    if (badgeId == 9999L) {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_BADGE.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_BADGE));
    }
    return ResponseEntity.ok(CommonApiResponse.success("뱃지 수정 완료"));
  }

  // ✅ 3. 뱃지 삭제
  @Operation(
      summary = "뱃지 삭제",
      description = "지정된 뱃지를 삭제합니다.",
      parameters = {
          @Parameter(name = "badgeId", description = "삭제할 뱃지 ID", required = true, example = "1")
      }
  )
  @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CommonApiResponse.class)))
  @DeleteMapping("/badge/{badgeId}")
  public ResponseEntity<?> deleteBadge(@PathVariable Long badgeId, Authentication authentication) {
    if (badgeId == 9999L) {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_BADGE.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_BADGE));
    }
    return ResponseEntity.ok(CommonApiResponse.success("뱃지 삭제 완료"));
  }

}
