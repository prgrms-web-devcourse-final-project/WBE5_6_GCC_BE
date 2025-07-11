package com.honlife.core.app.controller.member.admin;

import com.honlife.core.app.model.item.dto.ItemCreateRequestDTO;
import com.honlife.core.app.model.item.dto.ItemUpdateRequestDTO;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="관리자 상점 관리", description = "관리자 상점 관련 API 입니다.")
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminItemController {

  //상점 아이템 추가하기
  @Operation(
      summary = "상점 아이템 추가",
      description = "상점 아이템을 추가합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ItemCreateRequestDTO.class)

          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PostMapping("/items")
  public ResponseEntity<?> addStoreItem(@RequestBody ItemCreateRequestDTO request, Authentication authentication) {
    if (request.getItemKey() == null || request.getName() == null) {
      return ResponseEntity.status(ResponseCode.BAD_REQUEST.status())
          .body(CommonApiResponse.error(ResponseCode.BAD_REQUEST));
    }

    return ResponseEntity.ok(CommonApiResponse.success("상점 아이템 추가 완료"));
  }


  //상점 아이템 수정
  @Operation(
      summary = "상점 아이템 수정",
      description = "상점 아이템을 수정합니다.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          content = @Content(
              mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = ItemUpdateRequestDTO.class)
          )
      )
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @PatchMapping("/items/{itemId}")
  public ResponseEntity<?> modifyStoreItem(
      @PathVariable Long itemId,
      @RequestBody ItemUpdateRequestDTO request,
      Authentication authentication
  ) {
    if (itemId == 10L) {
      return ResponseEntity.ok(CommonApiResponse.success("상점 아이템 수정 완료"));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_ITEM.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_ITEM));
    }
  }


  @Operation(
      summary = "상점 아이템 삭제",
      description = "상점 아이템을 삭제합니다.",
      parameters = {
          @Parameter(
              name = "itemid",
              description = "삭제할 아이템의 ID",
              required = true,
              example = "1"
          )
      }
  )
  @ApiResponse(
      responseCode="200",
      content=@Content(
          mediaType="application/json",
          schema=@Schema(implementation= CommonApiResponse.class)
      )
  )
  @DeleteMapping("/items/{itemId}")
  public ResponseEntity<?> deleteStoreItem(@PathVariable Long itemId, Authentication authentication) {
    if (itemId == 10L) {
      return ResponseEntity.ok(CommonApiResponse.success("상점 아이템 삭제 완료"));
    } else {
      return ResponseEntity.status(ResponseCode.NOT_EXIST_ITEM.status())
          .body(CommonApiResponse.error(ResponseCode.NOT_EXIST_ITEM));
    }
  }

}
