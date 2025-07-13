package com.honlife.core.app.controller.admin.badge;

import com.honlife.core.app.controller.admin.badge.payload.AdminBadgeSaveRequest;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@Tag(name="관리자 업적 관리", description = "관리자 업적 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminBadgeController {

  /**
   * 새로운 업적을 등록하는 API
   * @param badgeSaveRequest 새로운 업적 정보를 담은 request
   * @param bindingResult validation
   * @return
   */
  @Operation(
      summary = "업적 등록",
      description = "새로운 업적을 등록합니다.<br>"
          + "tier에는 BRONZE, SILVER, GOLD, PLATINUM가 들어갈 수 있으며<br>"
          + "key, name, how, requirement 는 필수로 작성하여야 합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = AdminBadgeSaveRequest.class)
          )
      )
  )
  @PostMapping
  public ResponseEntity<CommonApiResponse<Void>> registerBadge(
      @RequestBody @Valid AdminBadgeSaveRequest badgeSaveRequest,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity
          .status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }


  @Operation(
      summary = "업적 수정",
      description = "기존 업적 정보를 수정합니다.<br>"
          + "tier에는 BRONZE, SILVER, GOLD, PLATINUM가 들어갈 수 있으며<br>"
          + "key, name, how, requirement 는 필수로 작성하여야 합니다.<br><br>"
          + "아이디가 1에 해당하는 업적에 대한 수정 요청만 가능합니다.",
      parameters = {
          @Parameter(name = "id", description = "수정할 뱃지 ID", required = true, example = "1")
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = AdminBadgeSaveRequest.class)
          )
      )
  )
  @PatchMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> updateBadge(
      @PathVariable(name="id") Long badgeId,
      @RequestBody AdminBadgeSaveRequest badgeSaveRequest,
      BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity
          .status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    if (badgeId != 1L) {
      return ResponseEntity
          .status(ResponseCode.NOT_FOUND_BADGE.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
    }

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }


  @Operation(
      summary = "업적 삭제",
      description = "지정된 업적을 삭제합니다.<br>"
          + "id가 1에 해당하는 업적에 대한 삭제요청만 가능합니다.",
      parameters = {
          @Parameter(name = "id", description = "삭제할 업적 ID", required = true, example = "1")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> deleteBadge(
      @PathVariable(name="id") Long badgeId
  ) {
    if (badgeId != 1L) {
      return ResponseEntity
          .status(ResponseCode.NOT_FOUND_BADGE.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
    }

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }


}
