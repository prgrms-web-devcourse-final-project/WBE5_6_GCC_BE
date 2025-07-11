package com.honlife.core.app.controller.member.admin;

import com.honlife.core.app.model.quest.dto.WeeklyQuestRequestDTO;
import com.honlife.core.app.model.quest.dto.WeeklyQuestUpdateRequestDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="관리자 주간 퀘스트 관리", description = "관리자 주간 퀘스트 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminWeeklyQuestController {

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
    if (request.getKey() == null || request.getName() == null) {
      return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    return ResponseEntity.ok(CommonApiResponse.success("주간 퀘스트 등록 성공"));
  }


  // 주간 퀘스트 수정
  @Operation(
      summary = "주간 퀘스트 수정",
      description = "주간 퀘스트를 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = WeeklyQuestUpdateRequestDTO.class)
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
  @PatchMapping("/quest/weekly/{weeklyId}")
  public ResponseEntity<?> weeklyModify(
      @PathVariable Long weeklyId,
      @RequestBody WeeklyQuestUpdateRequestDTO request,
      Authentication authentication
  ) {
    if (weeklyId == 9999L) {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_QUEST));
    }

    return ResponseEntity.ok(CommonApiResponse.success("주간 퀘스트 수정 완료"));
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
  @DeleteMapping("/quest/weekly/{weeklyId}")
  public ResponseEntity<?> weeklyDelete(@PathVariable Long weeklyId, Authentication authentication) {
    if (weeklyId == 9999L) {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_QUEST));
    }

    return ResponseEntity.ok(CommonApiResponse.success("주간 퀘스트 삭제 완료"));
  }



}
