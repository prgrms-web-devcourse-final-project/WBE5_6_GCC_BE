package com.honlife.core.app.controller.admin.log.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LoginLogResponse {

    private Long loginLogId;

    private Long memberId;

    private LocalDateTime time;
}
