package com.honlife.core.app.model.dashboard.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDashboardDTO {

    private Long id;

    private Long member;

    private LocalDate startDate;

    private String aiComment;

}
