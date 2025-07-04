package spring.grepp.honlife.point_policy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PointPolicyDTO {

    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    private PointSourceType type;

    @Size(max = 50)
    private String referenceKey;

    private Integer point;

}
