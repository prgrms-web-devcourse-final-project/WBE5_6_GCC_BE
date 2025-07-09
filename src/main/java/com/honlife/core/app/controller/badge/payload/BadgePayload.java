package com.honlife.core.app.controller.badge.payload;

import com.honlife.core.app.model.badge.code.BadgeTier;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * 모든 업적을 조회할 때 반환 되는 응답 클래스.
 * 업적 정보와 사용자 보유 여부를 포함함.
 */
@Getter
@Setter
@Builder
@Schema(
    name    = "BadgePayload",
    example = """
    {
      "badgeId": 1,
      "badgeKey": "clean_bronze",
      "badgeName": "초보 청소부",
      "tier": "BRONZE",
      "how": "청소 루틴 5번 이상 성공",
      "requirement": 5,
      "info": "이제 청소 좀 한다고 말할 수 있겠네요!",
      "category": "청소",
      "isReceived": false
    }
  """
)
public class BadgePayload {

    private Long badgeId;

    private String badgeKey;

    private String badgeName;

    private BadgeTier tier;

    private String how;

    private Integer requirement;

    private String info;

    private String category;

    private Boolean isReceived;


}
