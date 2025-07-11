package com.honlife.core.app.model.loginLog.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class LoginLogListDTO {
  private Long id;

  private LocalDateTime time;


  private String name;

}
