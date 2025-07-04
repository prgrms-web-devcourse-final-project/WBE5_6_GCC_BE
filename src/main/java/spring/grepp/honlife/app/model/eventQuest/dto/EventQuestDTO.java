package spring.grepp.honlife.app.model.eventQuest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EventQuestDTO {

    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @Size(max = 50)
    private String key;

    @Size(max = 255)
    private String name;

    private String info;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

}
