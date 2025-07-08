package com.honlife.core.app.model.point.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import com.honlife.core.app.model.point.code.PointLogType;


@Getter
@Setter
public class PointLogDTO {

    private Long id;

    private PointLogType type;

    private Integer point;

    @Size(max = 255)
    private String reason;

    private LocalDateTime time;

    @NotNull
    private Long member;

}
