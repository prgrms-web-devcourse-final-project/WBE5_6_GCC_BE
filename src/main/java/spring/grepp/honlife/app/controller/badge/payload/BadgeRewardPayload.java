package spring.grepp.honlife.app.controller.badge.payload;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * 특정 업적을 클리어 할 시 반환 되는 클래스
 * 해당 업적에 대한 정보와 최종 포인트, 받은 시각을 포함함.
 */

@Getter
@Setter
@Builder
public class BadgeRewardPayload {

    private Long id;

    private String key;

    private String name;

    private Long pointAdded;

    private Long totalPoint;

    private LocalDateTime receivedAt;

}
