package com.honlife.core.app.controller.admin;

import com.honlife.core.app.controller.admin.payload.EventQuestRequestDTO;
import com.honlife.core.app.controller.admin.payload.EventQuestUpdateRequestDTO;
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
@Tag(name="관리자 이벤트 퀘스트 관리", description = "관리자 이벤트 퀘스트 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminEventQuestController {


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
  @PostMapping("/quest/event")
  public ResponseEntity<CommonApiResponse<String>> eventRegister(
      @RequestBody EventQuestRequestDTO request,
      Authentication authentication
  ) {
    if ("event_key_10".equals(request.getKey())) {
      return ResponseEntity.ok(CommonApiResponse.success("이벤트 퀘스트 등록 성공"));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_EVENT_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_EVENT_QUEST));
    }
  }

  @Operation(
      summary = "이벤트 퀘스트 수정",
      description = "이벤트 퀘스트를 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = EventQuestUpdateRequestDTO.class)
          )
      )
  )
  @PatchMapping("/quest/event/{eventId}")
  public ResponseEntity<CommonApiResponse<String>> eventModify(
      @PathVariable Long eventId,
      @RequestBody EventQuestUpdateRequestDTO request,
      Authentication authentication
  ) {
    if (eventId == 10L) {
      return ResponseEntity.ok(CommonApiResponse.success("이벤트 퀘스트 수정 완료"));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_EVENT_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_EVENT_QUEST));
    }
  }

  @Operation(
      summary = "이벤트 퀘스트 삭제",
      description = "이벤트 퀘스트를 삭제합니다.",
      parameters = {
          @Parameter(
              name = "eventId",
              description = "삭제할 퀘스트 ID",
              required = true,
              example = "10"
          )
      }
  )
  @DeleteMapping("/quest/event/{eventId}")
  public ResponseEntity<CommonApiResponse<String>> eventDelete(
      @PathVariable Long eventId,
      Authentication authentication
  ) {
    if (eventId == 10L) {
      return ResponseEntity.ok(CommonApiResponse.success("이벤트 퀘스트 삭제 완료"));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_EVENT_QUEST.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_EVENT_QUEST));
    }
  }


}
