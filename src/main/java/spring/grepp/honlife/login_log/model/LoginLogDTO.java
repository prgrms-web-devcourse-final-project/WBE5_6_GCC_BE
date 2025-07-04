package spring.grepp.honlife.login_log.model;

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
