package com.honlife.core.app.controller.dashboard;

import com.honlife.core.app.controller.dashboard.warpper.MemberDashboardWrapper;
import com.honlife.core.infra.response.CommonApiResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/members/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardController {

    @GetMapping
    public ResponseEntity<CommonApiResponse<Void>> getReportData(
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startDate
    ){
        return null;
    }

}
