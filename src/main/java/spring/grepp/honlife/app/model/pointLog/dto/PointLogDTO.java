package spring.grepp.honlife.app.model.pointLog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import spring.grepp.honlife.app.model.pointLog.code.PointLogType;


@Getter
@Setter
public class PointLogDTO {

    private Integer id;

    private PointLogType type;

    private Integer point;

    @Size(max = 255)
    private String reason;

    private OffsetDateTime time;

    @NotNull
    private Integer member;

}
