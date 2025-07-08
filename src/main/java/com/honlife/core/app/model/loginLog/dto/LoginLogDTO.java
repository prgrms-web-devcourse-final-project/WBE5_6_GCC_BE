package com.honlife.core.app.model.loginLog.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginLogDTO {

    private Long id;

    private LocalDateTime time;

    @NotNull
    private Long member;

}
