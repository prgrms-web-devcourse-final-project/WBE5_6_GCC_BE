package spring.grepp.honlife.app.model.memberItem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberItemDTO {

    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @JsonProperty("isEquipped")
    private Boolean isEquipped;

    @NotNull
    private Integer member;

    @NotNull
    private Integer item;

}
