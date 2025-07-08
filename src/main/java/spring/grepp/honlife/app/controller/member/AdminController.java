package spring.grepp.honlife.app.controller.member;

import io.micrometer.common.KeyValue;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.grepp.honlife.infra.Response.ApiResponse;

@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {


  @PostMapping("/quest/weekly")
  public ResponseEntity<?> weeklyRegister(){

    return ResponseEntity.ok(new ApiResponse(2000, "주간 퀘스트 등록 성공"));
  }

  @PostMapping("/quest/event")
  public ResponseEntity<?> eventRegister(){

    return ResponseEntity.ok(new ApiResponse(2000, "이벤트 퀘스트 등록 성공"));
  }

  @PatchMapping("/quest/weekly/{id}")
  public ResponseEntity<?> weeklyModify(){

    return ResponseEntity.ok(new ApiResponse(2000, "주간 퀘스트 수정 완료"));
  }

  @PatchMapping("/quest/event/{id}")
  public ResponseEntity<?> eventModify(){

    return ResponseEntity.ok(new ApiResponse(2000, "이벤트 퀘스트 수정 완료"));
  }

  @DeleteMapping("/quest/weekly/{id}")
  public ResponseEntity<?> weeklyDelete(){

    return ResponseEntity.ok(new ApiResponse(2000, "주간 퀘스트 삭제 완료"));
  }

  @DeleteMapping("/quest/event/{id}")
  public ResponseEntity<?> eventDelete(){

    return ResponseEntity.ok(new ApiResponse(2000, "이벤트 퀘스트 삭제 완료"));
  }

  @GetMapping("/log/login")
  public ResponseEntity<?> getLoginLogsByMember(){

    return ResponseEntity.ok("여기다가 해당 dto 넣기");
  }

  //포인트/소비기록 조회
  @GetMapping("/log/point")
  public ResponseEntity<?> getPointLogs(){

    return ResponseEntity.ok("여기다가 해당 dto 넣기");
  }

  //포인트/소비기록 삭제
  @DeleteMapping("/log/point")
  public ResponseEntity<?> pointLogsDelete(){

    return ResponseEntity.ok("여기다가 해당 dto 넣기");
  }

  //추천 루틴 추가
  @PostMapping("/preset")
  public ResponseEntity<?> addRoutinePreset(){

    return ResponseEntity.ok(new ApiResponse(2000, "추천 루틴 추가 완료"));
  }

  //추천 루틴 수정
  @PatchMapping("/preset/{presetId}")
  public ResponseEntity<?> modifyRoutinePreset(){

    return ResponseEntity.ok(new ApiResponse(2000, "추천 루틴 수정 완료"));
  }

  //추천 루틴 삭제
  @DeleteMapping("/preset/{presetId}")
  public ResponseEntity<?> deleteRoutinePreset(){

    return ResponseEntity.ok(new ApiResponse(2000, "추천 루틴 삭제 완료"));
  }

  //상점 아이템 추가
  @PostMapping("/items")
  public ResponseEntity<?> addStoreItem(){

    return ResponseEntity.ok("여기다가 해당 dto 넣기");
  }

  //상점 아이템 수정
  @PatchMapping("/items/{itemid}")
  public ResponseEntity<?> modifyStoreItem(){

    return ResponseEntity.ok(new ApiResponse(2000, "상점 아이템 수정 완료"));
  }

  //상점 아이템 삭제
  @DeleteMapping("/items/{itemid}")
  public ResponseEntity<?> deleteStoreItem(){

    return ResponseEntity.ok(new ApiResponse(2000, "상점 아이템 삭제 완료"));
  }

}
