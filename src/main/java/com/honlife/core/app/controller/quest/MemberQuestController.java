package com.honlife.core.app.controller.quest;

import com.honlife.core.app.controller.quest.payload.EventQuestProgressResponse;
import com.honlife.core.app.controller.quest.payload.WeeklyQuestProgressResponse;
import com.honlife.core.app.controller.quest.wrapper.AllQuestsProgressWrapper;
import com.honlife.core.app.model.quest.service.EventQuestProgressService;
import com.honlife.core.app.model.quest.service.EventQuestService;
import com.honlife.core.app.model.quest.service.WeeklyQuestService;
import com.honlife.core.infra.response.CommonApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.honlife.core.app.model.quest.service.WeeklyQuestProgressService;

@RestController
@RequestMapping(value = "/api/v1/members/quests", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberQuestController {

    private final WeeklyQuestProgressService weeklyQuestProgressService;
    private final EventQuestProgressService eventQuestProgressService;
    private final WeeklyQuestService weeklyQuestService;
    private final EventQuestService eventQuestService;

    /**
     * 로그인한 회원이 보유하고 있는 퀘스트 현황 조회
     * @return MemberQuestResponse
     */
    @GetMapping
    public ResponseEntity<CommonApiResponse<AllQuestsProgressWrapper>> getMemberQuests(
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();

        List<WeeklyQuestProgressResponse> weeklyQuestProgressResponse = WeeklyQuestProgressResponse.fromDTOList(
            weeklyQuestProgressService.getMemberWeeklyQuestsProgress(userEmail)
        );
        List<EventQuestProgressResponse> eventQuestProgressResponse = EventQuestProgressResponse.fromDTOList(
            eventQuestProgressService.getMemberEventQuestsProgress(userEmail)
        );

        return ResponseEntity.ok().body(CommonApiResponse.success(new AllQuestsProgressWrapper(weeklyQuestProgressResponse, eventQuestProgressResponse)));
    }
}
