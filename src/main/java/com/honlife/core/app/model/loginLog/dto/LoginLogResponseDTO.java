package com.honlife.core.app.model.loginLog.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogResponseDTO {

  private Long loginLogId;

  private String email;

  private LocalDateTime time;
}
