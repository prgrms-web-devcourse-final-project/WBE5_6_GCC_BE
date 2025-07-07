package spring.grepp.honlife.app.model.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoutineDTO {

    private Integer id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty("isActive")
    private Boolean isActive;

    @Size(max = 255)
    private String content;

    @Size(max = 255)
    private String triggerTime;

    @JsonProperty("isDone")
    private Boolean isDone;

    @JsonProperty("isImportant")
    private Boolean isImportant;

    @Size(max = 20)
    private String repeateType;

    @Size(max = 100)
    private String repeateValue;

    @NotNull
    private Integer member;

    @NotNull
    private Integer category;

}
