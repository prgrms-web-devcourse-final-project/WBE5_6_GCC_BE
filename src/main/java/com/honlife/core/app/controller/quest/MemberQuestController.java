package com.honlife.core.app.controller.quest;

import com.honlife.core.app.controller.quest.payload.EventQuestProgressResponse;
import com.honlife.core.app.controller.quest.payload.WeeklyQuestProgressResponse;
import com.honlife.core.app.controller.quest.wrapper.AllQuestsProgressWrapper;
import com.honlife.core.infra.response.CommonApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="✅ [일반] 퀘스트", description = "퀘스트 관련 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/members/quests", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberQuestController {

    @Operation(summary = "✅ 퀘스트 현황 조회", description = "로그인한 회원이 보유하고 있는 전체 퀘스트 현황을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonApiResponse<AllQuestsProgressWrapper>> getMemberQuests(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<WeeklyQuestProgressResponse> weeklyQuestProgressResponse = new ArrayList<>(List.of());
        weeklyQuestProgressResponse.add(
            WeeklyQuestProgressResponse.builder()
                .questId(1L)
                .progressId(1L)
                .categoryId(1L)
                .questKey("weekly_quest_1")
                .questName("퀘스트 1")
                .target(5)
                .progress(0)
                .build()
        );
        weeklyQuestProgressResponse.add(
            WeeklyQuestProgressResponse.builder()
                .questId(2L)
                .progressId(2L)
                .categoryId(2L)
                .questKey("weekly_quest_2")
                .questName("퀘스트 2")
                .target(5)
                .progress(2)
                .build()
        );
        weeklyQuestProgressResponse.add(
            WeeklyQuestProgressResponse.builder()
                .questId(3L)
                .progressId(3L)
                .categoryId(3L)
                .questKey("weekly_quest_3")
                .questName("퀘스트 3")
                .target(5)
                .progress(5)
                .build()
        );

        List<EventQuestProgressResponse> eventQuestProgressResponse = new ArrayList<>(List.of());
        eventQuestProgressResponse.add(
            EventQuestProgressResponse.builder()
                .questId(4L)
                .progressId(4L)
                .categoryId(4L)
                .questKey("event_quest_4")
                .questName("이벤트 4")
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusDays(1))
                .target(10)
                .progress(5)
                .build()
        );

        return ResponseEntity.ok().body(CommonApiResponse.success(new AllQuestsProgressWrapper(weeklyQuestProgressResponse, eventQuestProgressResponse)));
    }

    @Operation(summary = "✅ 완료된 퀘스트의 보상을 수령", description = "사용자가 완료된 퀘스트의 보상받기 버튼을 눌렀을때, 보상을 지급합니다.<br>"
        + "보상 받기를 눌렀을 때, DB에 사용자의 퀘스트가 완료로 저장되며, 사용자의 포인트에 퀘스트 보상 포인트가 합산됩니다.")
    @PostMapping("/{key}")
    public ResponseEntity<CommonApiResponse<Void>> getReward (
        @AuthenticationPrincipal UserDetails userDetails,
        @PathVariable(name="key") @Schema(description="퀘스트의 키값입니다.", example="weekly_quest_01") String questKey
    ) {
        return ResponseEntity.ok().body(CommonApiResponse.noContent());
    }
}
