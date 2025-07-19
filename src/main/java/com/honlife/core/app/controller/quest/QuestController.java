package com.honlife.core.app.controller.quest;

import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="[일반] 퀘스트", description = "퀘스트 관련 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/quests", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestController {

    @Operation(summary = "완료된 퀘스트의 보상을 수령", description = "사용자가 완료된 퀘스트의 보상받기 버튼을 눌렀을때, 보상을 지급합니다.<br>"
        + "보상 받기를 눌렀을 때, DB에 사용자의 퀘스트가 완료로 저장되며, 사용자의 포인트에 퀘스트 보상 포인트가 합산됩니다.")
    @PostMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> getReward (
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable(name="key") @Schema(description="퀘스트의 키값입니다.", example="weekly_quest_01") String questKey
    ) {
        return ResponseEntity.ok().body(CommonApiResponse.noContent());
    }
}
