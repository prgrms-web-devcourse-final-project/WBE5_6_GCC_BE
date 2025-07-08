package spring.grepp.honlife.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberImageDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @NotNull
    @Size(max = 50)
    private String savePath;

    @NotNull
    @Size(max = 36)
    private String type;

    @NotNull
    @Size(max = 255)
    private String originName;

    @NotNull
    @Size(max = 255)
    private String renamedName;

}
