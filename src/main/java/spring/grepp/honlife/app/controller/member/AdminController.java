package spring.grepp.honlife.app.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import spring.grepp.honlife.infra.Response.ApiEntityResponse;

@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

  @Operation(summary = "주간 퀘스트 등록", description = "주간 퀘스트를 등록합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "주간 퀘스트 등록 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"주간 퀘스트 등록 성공\"
              }
              """)))
  @PostMapping("/quest/weekly")
  public ResponseEntity<?> weeklyRegister() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "주간 퀘스트 등록 성공"));
  }

  @Operation(summary = "이벤트 퀘스트 등록", description = "이벤트 퀘스트를 등록합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "이벤트 퀘스트 등록 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"이벤트 퀘스트 등록 성공\"
              }
              """)))
  @PostMapping("/quest/event")
  public ResponseEntity<?> eventRegister() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "이벤트 퀘스트 등록 성공"));
  }

  @Operation(summary = "주간 퀘스트 수정", description = "주간 퀘스트를 수정합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "주간 퀘스트 수정 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"주간 퀘스트 수정 완료\"
              }
              """)))
  @PatchMapping("/quest/weekly/{id}")
  public ResponseEntity<?> weeklyModify() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "주간 퀘스트 수정 완료"));
  }

  @Operation(summary = "이벤트 퀘스트 수정", description = "이벤트 퀘스트를 수정합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "이벤트 퀘스트 수정 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"이벤트 퀘스트 수정 완료\"
              }
              """)))
  @PatchMapping("/quest/event/{id}")
  public ResponseEntity<?> eventModify() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "이벤트 퀘스트 수정 완료"));
  }

  @Operation(summary = "주간 퀘스트 삭제", description = "주간 퀘스트를 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "주간 퀘스트 삭제 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"주간 퀘스트 삭제 완료\"
              }
              """)))
  @DeleteMapping("/quest/weekly/{id}")
  public ResponseEntity<?> weeklyDelete() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "주간 퀘스트 삭제 완료"));
  }

  @Operation(summary = "이벤트 퀘스트 삭제", description = "이벤트 퀘스트를 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "이벤트 퀘스트 삭제 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"이벤트 퀘스트 삭제 완료\"
              }
              """)))
  @DeleteMapping("/quest/event/{id}")
  public ResponseEntity<?> eventDelete() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "이벤트 퀘스트 삭제 완료"));
  }

  @Operation(summary = "유저 접속 기록 조회", description = "유저의 로그인 기록을 반환합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "로그인 기록 예시", value = """
              [
                {
                  \"id\": 1,
                  \"member_id\": 123,
                  \"time\": \"2025-07-06T09:00:00Z\"
                },
                {
                  \"id\": 2,
                  \"member_id\": 123,
                  \"time\": \"2025-07-07T08:45:00Z\"
                }
              ]
              """)))
  @GetMapping("/log/login")
  public ResponseEntity<?> getLoginLogsByMember() {
    return ResponseEntity.ok("로그 기록 예시 응답");
  }

  @Operation(summary = "포인트 기록 조회", description = "포인트 지급 및 소비 기록을 반환합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "포인트 기록 예시", value = """
              [
                {
                  \"id\": 10,
                  \"member_id\": 123,
                  \"type\": \"GET\",
                  \"point\": 1000,
                  \"reason\": \"퀘스트 완료\",
                  \"time\": \"2025-07-05T14:00:00Z\"
                }
              ]
              """)))
  @GetMapping("/log/point")
  public ResponseEntity<?> getPointLogs() {
    return ResponseEntity.ok("포인트 로그 예시 응답");
  }

  @Operation(summary = "포인트 로그 삭제", description = "포인트 지급 및 소비 기록을 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "포인트 로그 삭제 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"포인트 로그 삭제 완료\"
              }
              """)))
  @DeleteMapping("/log/point")
  public ResponseEntity<?> pointLogsDelete() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "포인트 로그 삭제 완료"));
  }

  @Operation(summary = "추천 루틴 추가", description = "추천 루틴을 추가합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "추천 루틴 추가 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"추천 루틴 추가 완료\"
              }
              """)))
  @PostMapping("/preset")
  public ResponseEntity<?> addRoutinePreset() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "추천 루틴 추가 완료"));
  }

  @Operation(summary = "추천 루틴 수정", description = "추천 루틴을 수정합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "추천 루틴 수정 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"추천 루틴 수정 완료\"
              }
              """)))
  @PatchMapping("/preset/{presetId}")
  public ResponseEntity<?> modifyRoutinePreset() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "추천 루틴 수정 완료"));
  }

  @Operation(summary = "추천 루틴 삭제", description = "추천 루틴을 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "추천 루틴 삭제 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"추천 루틴 삭제 완료\"
              }
              """)))
  @DeleteMapping("/preset/{presetId}")
  public ResponseEntity<?> deleteRoutinePreset() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "추천 루틴 삭제 완료"));
  }

  @Operation(summary = "상점 아이템 추가", description = "상점 아이템을 추가합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "상점 아이템 추가 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"상점 아이템 추가 완료\"
              }
              """)))
  @PostMapping("/items")
  public ResponseEntity<?> addStoreItem() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "상점 아이템 추가 완료"));
  }

  @Operation(summary = "상점 아이템 수정", description = "상점 아이템을 수정합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "상점 아이템 수정 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"상점 아이템 수정 완료\"
              }
              """)))
  @PatchMapping("/items/{itemid}")
  public ResponseEntity<?> modifyStoreItem() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "상점 아이템 수정 완료"));
  }

  @Operation(summary = "상점 아이템 삭제", description = "상점 아이템을 삭제합니다.")
  @ApiResponse(responseCode = "200", description = "OK",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          examples = @ExampleObject(name = "상점 아이템 삭제 예시", value = """
              {
                \"status\": 2000,
                \"message\": \"상점 아이템 삭제 완료\"
              }
              """)))
  @DeleteMapping("/items/{itemid}")
  public ResponseEntity<?> deleteStoreItem() {
    return ResponseEntity.ok(new ApiEntityResponse(2000, "상점 아이템 삭제 완료"));
  }
}
