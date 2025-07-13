package com.honlife.core.app.controller.admin.badge;

import com.honlife.core.app.controller.admin.badge.payload.AdminBadgeResponse;
import com.honlife.core.app.controller.admin.badge.payload.AdminBadgeSaveRequest;
import com.honlife.core.app.model.badge.code.BadgeTier;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@Tag(name="관리자 업적 관리", description = "관리자 업적 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin/badges", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminBadgeController {

  /**
   * 업적 조회 API
   * @return List<BadgePayload> 모든 업적에 대한 정보
   */
  @Operation(summary = "업적 조회", description = "모든 업적의 정보를 조회합니다.")
  @GetMapping
  public ResponseEntity<CommonApiResponse<List<AdminBadgeResponse>>> getAllBadges(
      @Schema(description = "카테고리 아이디", example = "1")
      @RequestParam Long categoryId
  ) {
    // 해당하는 카테고리가 없는 업적
    if(categoryId == 0) {
      List<AdminBadgeResponse> responses = new ArrayList<>();
      responses.add(AdminBadgeResponse.builder()
          .badgeId(3L)
          .badgeKey("attendance_bronze")
          .badgeName("성실하답니다")
          .tier(BadgeTier.BRONZE)
          .how("연속 3번 출석")
          .requirement(3)
          .info("연속 3번으로 출석한 나, 꽤나 장한걸요?")
          .categoryName(null)
          .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
          .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
          .isActive(true)
          .build());
      return ResponseEntity.ok(CommonApiResponse.success(responses));
    }
    if(categoryId == 1) {
      List<AdminBadgeResponse> responses = new ArrayList<>();
      responses.add(AdminBadgeResponse.builder()
          .badgeId(1L)
          .badgeKey("clean_bronze")
          .badgeName("초보 청소부")
          .tier(BadgeTier.BRONZE)
          .how("청소 루틴 5번 이상 성공")
          .requirement(5)
          .info("이제 청소 좀 한다고 말할 수 있겠네요!")
          .categoryName("청소")
          .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
          .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
          .isActive(true)
          .build());
      return ResponseEntity.ok(CommonApiResponse.success(responses));
    }

    if(categoryId == 2) {
      List<AdminBadgeResponse> responses = new ArrayList<>();
      responses.add(AdminBadgeResponse.builder()
          .badgeId(2L)
          .badgeKey("cook_bronze")
          .badgeName("초보 요리사")
          .tier(BadgeTier.BRONZE)
          .how("요리 루틴 5번 이상 성공")
          .requirement(5)
          .info("나름 계란 프라이는 할 수 있다구요!")
          .categoryName("요리")
          .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
          .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
          .isActive(true)
          .build());
    }
    List<AdminBadgeResponse> responses = new ArrayList<>();
    responses.add(AdminBadgeResponse.builder()
        .badgeId(1L)
        .badgeKey("clean_bronze")
        .badgeName("초보 청소부")
        .tier(BadgeTier.BRONZE)
        .how("청소 루틴 5번 이상 성공")
        .requirement(5)
        .info("이제 청소 좀 한다고 말할 수 있겠네요!")
        .categoryName("청소")
        .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
        .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
        .isActive(true)
        .build());
    responses.add(AdminBadgeResponse.builder()
        .badgeId(2L)
        .badgeKey("cook_bronze")
        .badgeName("초보 요리사")
        .tier(BadgeTier.BRONZE)
        .how("요리 루틴 5번 이상 성공")
        .requirement(5)
        .info("나름 계란 프라이는 할 수 있다구요!")
        .categoryName("요리")
        .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
        .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
        .isActive(true)
        .build());

    return ResponseEntity.ok(CommonApiResponse.success(responses));
  }


  /**
   * id에 해당하는 특정 조건을 조회
   * @param badgeId 뱃지 id 값
   * @return AdminBadgeResponse
   */
  @Operation(summary = "업적 단건 조회", description = "id에 해당하는 업적에 대해 조회합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<CommonApiResponse<AdminBadgeResponse>> getBadge(
      @Schema(name="id", description="업적의 아이디 값", example = "1")
      @PathVariable(name="id") Long badgeId
  ) {
    if(badgeId==1){
      AdminBadgeResponse response = AdminBadgeResponse.builder()
          .badgeId(badgeId)
          .badgeKey("clean_bronze")
          .badgeName("초보 청소부")
          .tier(BadgeTier.BRONZE)
          .how("청소 루틴 5번 이상 성공")
          .requirement(5)
          .info("이제 청소 좀 한다고 말할 수 있겠네요!")
          .categoryName("청소")
          .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
          .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
          .isActive(true)
          .build();
      return ResponseEntity.ok(CommonApiResponse.success(response));
    }
    if(badgeId==2){
      AdminBadgeResponse response = AdminBadgeResponse.builder()
          .badgeId(badgeId)
          .badgeKey("cook_bronze")
          .badgeName("초보 요리사")
          .tier(BadgeTier.BRONZE)
          .how("요리 루틴 5번 이상 성공")
          .requirement(5)
          .info("나름 계란 프라이는 할 수 있다구요!")
          .categoryName("요리")
          .createTime(LocalDateTime.parse("2025-07-09T21:30:00"))
          .updateTime(LocalDateTime.parse("2025-07-13T21:30:00"))
          .isActive(true)
          .build();
      return ResponseEntity.ok(CommonApiResponse.success(response));
    }else{
      return ResponseEntity.status(ResponseCode.NOT_FOUND_BADGE.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
    }
  }

  /**
   * 새로운 업적을 등록하는 API
   * @param badgeSaveRequest 새로운 업적 정보를 담은 request
   * @return 등록 성공 시 ok, 누락된 정보가 있다면 Bad Request 응답
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
      @RequestBody @Valid AdminBadgeSaveRequest badgeSaveRequest
  ) {

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }

  /**
   * 기존 업적 정보를 수정하는 API
   * @param badgeId pathVariable로 쓰이는 업적 아이디
   * @param badgeSaveRequest 수정할 업적 정보를 담은 request
   * @return 수정 성공 시 ok, 존재하지 않는 ID일 경우 Not Found Badge, 누락된 정보가 있다면 Bad Request 응답
   */
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
      @RequestBody @Valid AdminBadgeSaveRequest badgeSaveRequest
  ) {

    if (badgeId != 1L) {
      return ResponseEntity
          .status(ResponseCode.NOT_FOUND_BADGE.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_BADGE));
    }

    return ResponseEntity.ok(CommonApiResponse.noContent());
  }

  /**
   * id에 해당하는 업적을 삭제하는 API
   * @param badgeId pathVariable로 쓰이는 업적 아이디
   * @return 삭제 성공 시 ok, 존재하지 않는 ID일 경우 Not Found Badge 응답
   */
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
