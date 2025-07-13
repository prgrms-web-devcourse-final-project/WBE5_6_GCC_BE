package com.honlife.core.app.controller.admin.payload;

import com.honlife.core.app.model.point.code.PointLogType;
import com.honlife.core.app.model.point.code.PointSourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PointLogResponse {

    private Long pointLogId;

    private Long memberId;

    private PointLogType pointLogType;

    private Integer point;

    private PointSourceType reason;

    private LocalDateTime time;
}