package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.badge.payload.BadgePayload;
import com.honlife.core.app.model.item.dto.ItemCreateRequestDTO;
import com.honlife.core.app.model.item.dto.ItemUpdateRequestDTO;
import com.honlife.core.app.model.loginLog.dto.LoginLogResponseDTO;
import com.honlife.core.app.model.point.dto.PointLogResponseDTO;
import com.honlife.core.app.model.quest.dto.EventQuestUpdateDto;
import com.honlife.core.app.model.quest.dto.WeeklyQuestUpdateDTO;
import com.honlife.core.app.model.quest.dto.EventQuestRequestDTO;
import com.honlife.core.app.model.quest.dto.WeeklyQuestRequestDTO;
import com.honlife.core.app.model.routine.dto.RoutinePresetRequestDTO;
import com.honlife.core.app.model.routine.dto.RoutinePresetUpdateDTO;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.honlife.core.infra.response.ApiEntityResponse;

@RestController
@Tag(name="관리자", description = "관리자 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

  @Operation(
      summary = "주간 퀘스트 등록",
      description = "주간 퀘스트를 등록합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = WeeklyQuestRequestDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PostMapping("/quest/weekly")
  public ResponseEntity<?> weeklyRegister(@RequestBody WeeklyQuestRequestDTO request, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success("주간 퀘스트 등록 성공"));
  }



  @Operation(
      summary = "이벤트 퀘스트 등록",
      description = "이벤트 퀘스트를 등록합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = EventQuestRequestDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PostMapping("/quest/event")
  public ResponseEntity<?> eventRegister(@RequestBody EventQuestRequestDTO request, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success("이벤트 퀘스트 등록 성공"));
  }



  // 주간 퀘스트 수정
  @Operation(
      summary = "주간 퀘스트 수정",
      description = "주간 퀘스트를 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = WeeklyQuestUpdateDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PatchMapping("/quest/weekly/{id}")
  public ResponseEntity<?> weeklyModify(
      @PathVariable Long id,
      @RequestBody WeeklyQuestUpdateDTO request,
      Authentication authentication
  ) {

    return ResponseEntity.ok(CommonApiResponse.success("주간 퀘스트 수정 완료"));
  }

  //이벤트 퀘스트 수정
  @Operation(
      summary = "이벤트 퀘스트 수정",
      description = "이벤트 퀘스트를 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = EventQuestUpdateDto.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PatchMapping("/quest/event/{id}")
  public ResponseEntity<?> eventModify(
      @PathVariable Long id,
      @RequestBody EventQuestUpdateDto request,
      Authentication authentication
  ) {

    return ResponseEntity.ok(CommonApiResponse.success("이벤트 퀘스트 수정 완료"));
  }


  //주간 퀘스트 삭제
  @Operation(
      summary = "주간 퀘스트 삭제",
      description = "주간 퀘스트를 삭제합니다.",
      parameters = {
          @Parameter(
              name = "id",
              description = "삭제할 퀘스트 ID",
              required = true,
              example = "1"
          )
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @DeleteMapping("/quest/weekly/{id}")
  public ResponseEntity<?> weeklyDelete(@PathVariable Long id, Authentication authentication) {
    return ResponseEntity.ok(CommonApiResponse.success("주간 퀘스트 삭제 완료"));
  }



  //이벤트 퀘스트 삭제
  @Operation(
      summary = "이벤트 퀘스트 삭제",
      description = "이벤트 퀘스트를 삭제합니다.",
      parameters = {
          @Parameter(
              name = "id",
              description = "삭제할 퀘스트 ID",
              required = true,
              example = "1"
          )
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @DeleteMapping("/quest/event/{id}")
  public ResponseEntity<?> eventDelete(@PathVariable Long id, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success("이벤트 퀘스트 삭제 완료"));
  }


  @Operation(
      summary = "유저 접속 기록 조회",
      description = "유저의 로그인 기록을 반환합니다."
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= LoginLogResponseDTO.class)
      )
  )
  @GetMapping("/log/login")
  public ResponseEntity<?> getLoginLogsByMember(Authentication authentication) {
    List<LoginLogResponseDTO> logs = List.of(
        new LoginLogResponseDTO(1L, "aakjdf@naver.com", LocalDateTime.parse("2025-07-06T09:00:00")),
        new LoginLogResponseDTO(2L, "aakjdf@naver.com", LocalDateTime.parse("2025-07-07T08:45:00"))
    );

    return ResponseEntity.ok(CommonApiResponse.success(logs));
  }



  @Operation(summary = "포인트 기록 조회", description = "포인트 지급 및 소비 기록을 반환합니다.")
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= PointLogResponseDTO.class)
      )
  )
  @GetMapping("/log/point")
  public ResponseEntity<?> getPointLogs(Authentication authentication) {
    List<PointLogResponseDTO> logs = List.of(
        new PointLogResponseDTO(10L, "aakjdf@naver.com", "GET", 1000, "퀘스트 완료", LocalDateTime.parse("2025-07-05T14:00:00")),
            new PointLogResponseDTO(10L, "aakjdf@naver.com", "GET", 1000, "퀘스트 완료", LocalDateTime.parse("2025-07-05T14:00:00"))
    );

    return ResponseEntity.ok(CommonApiResponse.success(logs));
  }


  //포인트 로그 삭제
  @Operation(
      summary = "포인트 로그 삭제",
      description = "포인트 지급 및 소비 기록을 삭제합니다.",
      parameters = {
          @Parameter(
              name = "id",
              description = "삭제할 포인트 로그 ID",
              required = true,
              example = "42"
          )
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @DeleteMapping("/log/point")
  public ResponseEntity<?> pointLogsDelete(@PathVariable Long id, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success("포인트 로그 삭제 완료"));
  }


  //추천 루틴 추가
  @Operation(
      summary = "추천 루틴 추가",
      description = "추천 루틴을 추가합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoutinePresetRequestDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PostMapping("/preset")
  public ResponseEntity<?> addRoutinePreset(@RequestBody RoutinePresetRequestDTO request, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success("추천 루틴 추가 완료"));
  }


  //추천 루틴 수정
  @Operation(
      summary = "추천 루틴 수정",
      description = "추천 루틴을 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = RoutinePresetUpdateDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PatchMapping("/preset/{presetId}")
  public ResponseEntity<?> modifyRoutinePreset(
      @PathVariable Long presetId,
      @RequestBody RoutinePresetUpdateDTO request,
      Authentication authentication
  ) {

    return ResponseEntity.ok(CommonApiResponse.success("추천 루틴 수정 완료"));
  }


  //추천 루틴 삭제
  @Operation(
      summary = "추천 루틴 삭제",
      description = "추천 루틴을 삭제합니다.",
      parameters = {
          @Parameter(
              name = "presetId",
              description = "삭제할 추천 루틴 ID",
              required = true,
              example = "1"
          )
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @DeleteMapping("/preset/{presetId}")
  public ResponseEntity<?> deleteRoutinePreset(@PathVariable Long presetId, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success( "추천 루틴 삭제 완료"));
  }


  //상점 아이템 추가하기
  @Operation(
      summary = "상점 아이템 추가",
      description = "상점 아이템을 추가합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ItemCreateRequestDTO.class)

          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PostMapping("/items")
  public ResponseEntity<?> addStoreItem(@RequestBody ItemCreateRequestDTO request, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success("상점 아이템 추가 완료"));
  }


  //상점 아이템 수정
  @Operation(
      summary = "상점 아이템 수정",
      description = "상점 아이템을 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ItemUpdateRequestDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PatchMapping("/items/{itemid}")
  public ResponseEntity<?> modifyStoreItem(
      @PathVariable Long itemid,
      @RequestBody ItemUpdateRequestDTO request,
      Authentication authentication
  ) {

    return ResponseEntity.ok(CommonApiResponse.success("상점 아이템 수정 완료"));
  }


  @Operation(
      summary = "상점 아이템 삭제",
      description = "상점 아이템을 삭제합니다.",
      parameters = {
          @Parameter(
              name = "itemid",
              description = "삭제할 아이템의 ID",
              required = true,
              example = "1"
          )
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @DeleteMapping("/items/{itemid}")
  public ResponseEntity<?> deleteStoreItem(@PathVariable Long itemid, Authentication authentication) {

    return ResponseEntity.ok(CommonApiResponse.success( "상점 아이템 삭제 완료"));
  }

}
