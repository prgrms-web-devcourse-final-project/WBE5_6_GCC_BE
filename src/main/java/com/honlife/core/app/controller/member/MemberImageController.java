package com.honlife.core.app.controller.member;

import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Tag(name = "회원 이미지", description = "회원 이미지 업로드 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/members", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearerAuth")
public class MemberImageController {


  @Operation(
      summary = "프로필 이미지 업로드",
      description = "사용자의 프로필 이미지를 업로드합니다."
  )
  @PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<CommonApiResponse<String>> uploadProfileImage(
      @RequestParam("image") MultipartFile image,
      Authentication authentication
  ) {
    if (image.isEmpty()) {
      return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    // 실제 업로드 처리 로직
    return ResponseEntity.ok(CommonApiResponse.success("이미지 업로드 성공"));
  }


}
