package spring.grepp.honlife.app.model.loginLog.dto;

import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginLogDTO {

    private Integer id;

    private OffsetDateTime time;

    @NotNull
    private Integer member;

}
