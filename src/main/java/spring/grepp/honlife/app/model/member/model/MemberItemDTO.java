package spring.grepp.honlife.app.model.member.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberItemDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isEquipped")
    private Boolean isEquipped;

    @NotNull
    private Long member;

    @NotNull
    private Long item;

}
