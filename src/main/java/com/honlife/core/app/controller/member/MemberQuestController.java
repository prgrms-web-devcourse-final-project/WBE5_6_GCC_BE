package com.honlife.core.app.controller.member;

import com.honlife.core.app.controller.member.payload.MemberQuestResponse;
import com.honlife.core.app.model.point.code.PointSourceType;
import com.honlife.core.infra.response.CommonApiResponse;
import com.honlife.core.infra.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;

@Tag(name="회원 보유 퀘스트", description = "현재 로그인한 회원이 보유하고 있는 퀘스트 관련 API 입니다.")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(value = "/api/v1/members/quests", produces = MediaType.APPLICATION_JSON_VALUE)

public class MemberQuestController {

    private final WeeklyQuestProgressService weeklyQuestProgressService;

    public MemberQuestController(final WeeklyQuestProgressService weeklyQuestProgressService) {
        this.weeklyQuestProgressService = weeklyQuestProgressService;
    }

    /**
     * 로그인한 회원이 보유하고 있는 퀘스트 현황 조회
     * @return MemberQuestResponse
     */
    @GetMapping
    @Operation(summary = "로그인된 회원의 보유 퀘스트 조회", description = "로그인 된 사용자의 보유 퀘스트를 조회합니다. <br>현재 user01@test.com으로 로그인 되어있을 때만 확인 가능합니다."
        + "<br>type을 입력하지 않으면 전체 조회를, 입력하면 type에 해당하는 퀘스트를 조회합니다.")
    public ResponseEntity<CommonApiResponse<List<MemberQuestResponse>>> getMemberQuests(
        @AuthenticationPrincipal UserDetails userDetails,
        @Parameter(description="퀘스트 타입", example = "WEEKLY", schema = @Schema(allowableValues = {"WEEKLY", "EVENT"}))
        @RequestParam(name = "type", required = false) PointSourceType questType
    ) {
        // 퀘스트 데이터들
        MemberQuestResponse weeklyQuest1 = MemberQuestResponse.builder()
                .questKey("weekly_quest_01")
                .questName("청소의 달인")
                .questReward(100)
                .questInfo("청소 루틴 4번 완료")
                .isDone(false)
                .questProgress(50)
                .build();
        MemberQuestResponse weeklyQuest2 = MemberQuestResponse.builder()
                .questKey("weekly_quest_02")
                .questName("루틴 학살자")
                .questReward(15000)
                .questInfo("루틴 10번 실패")
                .isDone(true)
                .questProgress(100)
                .build();
        MemberQuestResponse eventQuest1 = MemberQuestResponse.builder()
                .questKey("event_quest_01")
                .questName("sun goes down...")
                .questReward(1000)
                .questInfo("명상 하기 루틴 1회 완료")
                .isDone(false)
                .questProgress(0)
                .build();
        MemberQuestResponse eventQuest2 = MemberQuestResponse.builder()
            .questKey("event_quest_02")
            .questName("팝콘냠냠")
            .questReward(1000)
            .questInfo("영화 보기 루틴 1회 완료")
            .isDone(true)
            .questProgress(100)
            .build();

        if(userDetails.getUsername().equals("user01@test.com")) {
            if(questType==null) {
                List<MemberQuestResponse> response = List.of(weeklyQuest1,weeklyQuest2, eventQuest1, eventQuest2);
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            if(questType.equals(PointSourceType.WEEKLY)){
                List<MemberQuestResponse> response = List.of(weeklyQuest1,weeklyQuest2);
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
            if(questType.equals(PointSourceType.EVENT)){
                List<MemberQuestResponse> response = List.of(eventQuest1, eventQuest2);
                return ResponseEntity.ok(CommonApiResponse.success(response));
            }
        }
        return ResponseEntity.status(ResponseCode.UNAUTHORIZED.status())
            .body(CommonApiResponse.error(ResponseCode.UNAUTHORIZED));
    }
}
