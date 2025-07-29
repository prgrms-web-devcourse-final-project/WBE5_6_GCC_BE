package com.honlife.core.app.controller.dashboard.payload;

import com.honlife.core.app.model.dashboard.dto.DayRoutineCountDTO;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 해당 날짜의 루틴 수를 담은 response
 */
@Data
@Builder
public class DayRoutineCountResponse {

    private LocalDateTime date;

    private Double completionRate;

    public static DayRoutineCountResponse fromDTO(DayRoutineCountDTO dto) {

        Double completionRate = null;
        // 만약 그 날에 루틴이 없으면?? 그냥 완료율 없는 거로...
        if(dto.getTotalCount() != 0){
            completionRate = (dto.getCompletedCount() / (double) dto.getTotalCount())*100;
        }

        return DayRoutineCountResponse.builder()
            .date(dto.getDate())
            .completionRate(completionRate)
            .build();
    }
}
