package com.honlife.core.app.controller.quest.payload;

import com.honlife.core.app.model.quest.dto.MemberEventQuestDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventQuestProgressResponse {

    private Long questId;
    private Long progressId;
    private Long categoryId;
    private String questKey;
    private String questName;
    private Integer target;
    private Integer progress;
    private Boolean isRewarded;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static EventQuestProgressResponse fromDTO(MemberEventQuestDTO memberEventQuestDTO) {
        return EventQuestProgressResponse.builder()
            .questId(memberEventQuestDTO.getQuestId())
            .progressId(memberEventQuestDTO.getProgressId())
            .categoryId(memberEventQuestDTO.getCategoryId())
            .questKey(memberEventQuestDTO.getQuestKey())
            .questName(memberEventQuestDTO.getQuestName())
            .startAt(memberEventQuestDTO.getStartAt())
            .endAt(memberEventQuestDTO.getEndAt())
            .target(memberEventQuestDTO.getTarget())
            .progress(memberEventQuestDTO.getProgress())
            .isRewarded(memberEventQuestDTO.getIsDone())
            .build();
    }

    public static List<EventQuestProgressResponse> fromDTOList(List<MemberEventQuestDTO> memberEventQuestDTOList) {
        return memberEventQuestDTOList.stream().map(EventQuestProgressResponse::fromDTO).collect(
            Collectors.toList());
    }
}
