package spring.grepp.honlife.app.controller.badge.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import spring.grepp.honlife.app.model.badge.code.BadgeTier;

/**
 * 모든 업적을 조회할 때 반환 되는 응답 클래스.
 * 업적 정보와 사용자 보유 여부를 포함함.
 */
@Getter
@Setter
@Builder
public class BadgePayload {

    private Long id;

    private String key;

    private String name;

    private BadgeTier tier;

    private String how;

    private Integer requirement;

    private String info;

    private Long category;

    private Boolean isReceived;


}
