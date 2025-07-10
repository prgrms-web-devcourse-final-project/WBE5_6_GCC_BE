package com.honlife.core.app.model.loginLog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "유저 로그인 기록 응답 DTO")
public class LoginLogResponseDTO {

  @Schema(description = "로그 기록 ID", example = "1")
  private Long id;

  @Schema(description = "유저 이메일", example = "aakjdf@naver.com")
  private String email;

  @Schema(description = "로그인 시간", example = "2025-07-06T09:00:00")
  private LocalDateTime time;
}
