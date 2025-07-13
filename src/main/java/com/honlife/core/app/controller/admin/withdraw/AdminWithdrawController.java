package com.honlife.core.app.controller.admin.withdraw;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="관리자 - 회원 탈퇴 이유", description = "관리자용 회원 탈퇴 이유 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminWithdrawController {

}
