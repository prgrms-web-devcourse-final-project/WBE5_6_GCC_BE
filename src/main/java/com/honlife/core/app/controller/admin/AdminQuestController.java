package com.honlife.core.app.controller.admin;

import com.honlife.core.app.controller.admin.payload.QuestDetailResponse;
import com.honlife.core.app.controller.admin.payload.QuestRequest;
import com.honlife.core.app.controller.admin.payload.QuestResponse;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

/**
 * 관리자 전용 퀘스트 관리 컨트롤러입니다.
 * EVENT, WEEKLY 타입 퀘스트에 대한 CRUD 작업을 처리합니다.
 */
@PreAuthorize("hasRole('ADMIN')")
@RestController
@Tag(name = "관리자 퀘스트 관리", description = "EVENT / WEEKLY 퀘스트 통합 관리 API입니다.")
@RequestMapping(value = "/api/v1/admin/quests/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminQuestController {

  /**
   * 퀘스트 타입별 목록을 조회합니다.
   *
   * @param type 퀘스트 타입 (EVENT 또는 WEEKLY)
   * @return 퀘스트 목록 응답
   */
  @Operation(summary = "퀘스트 목록 조회", description = "퀘스트 타입별 목록을 조회합니다. 지원 타입: EVENT, WEEKLY")
  @GetMapping
  public ResponseEntity<CommonApiResponse<List<QuestResponse>>> getQuestsByType(
      @Parameter(description = "퀘스트 타입", example = "EVENT")
      @PathVariable PointSourceType type
  ) {
    if (type != PointSourceType.EVENT && type != PointSourceType.WEEKLY) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
    List<QuestResponse> list = List.of(
        QuestResponse.builder()
            .questId(1L)
            .name("청소 루틴 3번 완료하기")
            .reward(100)
            .categoryId(1)
            .startDate(OffsetDateTime.parse("2025-07-01T00:00:00+09:00"))
            .endDate(OffsetDateTime.parse("2025-07-12T23:59:59+09:00"))
            .build()
    );

    return ResponseEntity.ok(CommonApiResponse.success(list));
  }

  /**
   * 특정 ID의 퀘스트 상세 정보를 조회합니다.
   *
   * @param type 퀘스트 타입 (EVENT 또는 WEEKLY)
   * @param id   퀘스트 ID
   * @return 퀘스트 상세 응답
   */
  @Operation(summary = "퀘스트 상세 조회", description = "퀘스트 타입과 ID에 해당하는 퀘스트 상세 정보를 반환합니다.")
  @GetMapping("/{id}")
  public ResponseEntity<CommonApiResponse<QuestDetailResponse>> getQuestDetail(
      @PathVariable PointSourceType type,
      @Parameter(description = "조회할 퀘스트 ID", example = "1")
      @PathVariable Long id
  ) {
    if (type != PointSourceType.EVENT && type != PointSourceType.WEEKLY) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
    if (id == 1L) {
      QuestDetailResponse response = QuestDetailResponse.builder()
          .questId(1L)
          .key("weekly_clean_3times")
          .type(type)
          .name("청소 루틴 3번 완료하기")
          .info("정해진 청소 루틴을 일주일에 3회 완료하세요.")
          .reward(100)
          .startDate(OffsetDateTime.parse("2025-07-01T00:00:00+09:00"))
          .endDate(OffsetDateTime.parse("2025-07-12T23:59:59+09:00"))
          .build();

      return ResponseEntity.ok(CommonApiResponse.success(response));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_FOUND_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_QUEST));
    }
  }

  /**
   * 새로운 퀘스트를 생성합니다.
   *
   * @param type    퀘스트 타입 (EVENT 또는 WEEKLY)
   * @param request 퀘스트 생성 요청 본문
   * @return 생성 결과 응답
   */
  @Operation(summary = "퀘스트 생성", description = "퀘스트를 생성합니다. 키가 중복되면 409 응답을 반환합니다.")
  @PostMapping
  public ResponseEntity<CommonApiResponse<Void>> createQuest(
      @PathVariable PointSourceType type,
      @RequestBody @Valid QuestRequest request
  ) {
    if (type != PointSourceType.EVENT && type != PointSourceType.WEEKLY) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
    if ("event_key_10".equals(request.getKey())) {
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(CommonApiResponse.noContent());
    } else {
      return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
  }

  /**
   * 특정 ID의 퀘스트를 수정합니다.
   *
   * @param type    퀘스트 타입 (EVENT 또는 WEEKLY)
   * @param id      수정할 퀘스트 ID
   * @param request 수정 요청 본문
   * @return 수정 결과 응답
   */
  @Operation(summary = "퀘스트 수정", description = "ID에 해당하는 퀘스트를 수정합니다.")
  @PatchMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> updateQuest(
      @PathVariable PointSourceType type,
      @Parameter(description = "수정할 퀘스트 ID", example = "1")
      @PathVariable Long id,
      @RequestBody @Valid QuestRequest request
  ) {
    if (type != PointSourceType.EVENT && type != PointSourceType.WEEKLY) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
    if (id == 1L) {
      return ResponseEntity.ok(CommonApiResponse.noContent());
    } else {
      return ResponseEntity.status(ResponseCode.NOT_FOUND_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_QUEST));
    }
  }

  /**
   * 특정 ID의 퀘스트를 삭제합니다.
   *
   * @param type 퀘스트 타입 (EVENT 또는 WEEKLY)
   * @param id   삭제할 퀘스트 ID
   * @return 삭제 결과 응답
   */
  @Operation(summary = "퀘스트 삭제", description = "ID에 해당하는 퀘스트를 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<CommonApiResponse<Void>> deleteQuest(
      @PathVariable PointSourceType type,
      @Parameter(description = "삭제할 퀘스트 ID", example = "10")
      @PathVariable Long id
  ) {
    if (type != PointSourceType.EVENT && type != PointSourceType.WEEKLY) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }
    if (id == 10L) {
      return ResponseEntity.ok(CommonApiResponse.noContent());
    } else {
      return ResponseEntity.status(ResponseCode.NOT_FOUND_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_FOUND_QUEST));
    }
  }
}
