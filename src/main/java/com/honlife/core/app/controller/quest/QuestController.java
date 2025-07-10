package com.honlife.core.app.controller.quest;

import com.honlife.core.app.model.quest.dto.QuestDetailResponse;
import com.honlife.core.infra.response.ApiEntityResponse;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/quests", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestController {

  @Operation(
      summary = "퀘스트 상세 조회",
      description = "퀘스트 타입(WEEKLY, EVENT)과 선택적 key 값을 기준으로 퀘스트 목록을 조회합니다.",
      parameters = {
          @Parameter(name = "type", description = "퀘스트 타입 (WEEKLY 또는 EVENT)", required = true, example = "WEEKLY"),
          @Parameter(name = "key", description = "퀘스트 고유 키값 (선택)", required = false, example = "weekly_clean_3times")
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @GetMapping("/quest/{type}")
  public ResponseEntity<?> getQuestsByTypeAndKey(
      @PathVariable String type,
      @RequestParam(required = false) String key,
      Authentication authentication
  ) {
    List<QuestDetailResponse> logs = List.of(
        new QuestDetailResponse(1L, "weekly_clean_3times", "WEEKLY", "청소 루틴 3번 완료하기", "정해진 청소 루틴을 일주일에 3회 완료하세요.", 100, 1, "2025-07-15"),
        new QuestDetailResponse(2L, "weekly_clean_3times", "WEEKLY", "청소 루틴 3번 완료하기", "정해진 청소 루틴을 일주일에 3회 완료하세요.", 100, 1, "2025-07-15")
    );
    return ResponseEntity.ok(CommonApiResponse.success(logs));
  }

  @Operation(
      summary = "퀘스트 완료 처리",
      description = "퀘스트를 완료 처리합니다.",
      parameters = {
          @Parameter(name = "type", description = "퀘스트 타입 (WEEKLY 또는 EVENT)", required = true, example = "WEEKLY"),
          @Parameter(name = "key", description = "퀘스트 고유 키값", required = true, example = "weekly_do_clean_3T")
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PostMapping("/quest/{type}")
  public ResponseEntity<?> completeQuest(
      @PathVariable String type,
      @RequestParam String key,
      Authentication authentication
  ) {
    // reward 값은 실제 처리 결과에 따라 바뀔 수 있음
    int reward = 200;

    Map<String, Object> data = new HashMap<>();
    data.put("reward", reward);

    return ResponseEntity.ok(new ApiEntityResponse(2000, "OK", data));
  }


}
