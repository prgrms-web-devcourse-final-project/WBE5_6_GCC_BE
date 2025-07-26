package com.honlife.core.app.controller.badge.payload;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

/**
 * 배지 페이지네이션 응답 클래스
 */
@Getter
@Setter
@Builder
public class BadgePageResponse {

    private List<BadgeResponse> content;        // 현재 페이지의 배지 목록
    private long totalElements;                 // 전체 배지 수
    private int totalPages;                     // 전체 페이지 수
    private int currentPage;                    // 현재 페이지 (1-based)
    private int size;                          // 페이지 크기
    private boolean hasNext;                   // 다음 페이지 존재 여부
    private boolean hasPrevious;               // 이전 페이지 존재 여부
    private boolean first;                     // 첫 번째 페이지 여부
    private boolean last;                      // 마지막 페이지 여부

    /**
     * Spring Data Page 객체와 BadgeResponse 리스트로부터 BadgePageResponse 생성
     */
    public static BadgePageResponse from(Page<?> page, List<BadgeResponse> content) {
        return BadgePageResponse.builder()
            .content(content)
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber() + 1)  // 0-based → 1-based 변환
            .size(page.getSize())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .first(page.isFirst())
            .last(page.isLast())
            .build();
    }
}
