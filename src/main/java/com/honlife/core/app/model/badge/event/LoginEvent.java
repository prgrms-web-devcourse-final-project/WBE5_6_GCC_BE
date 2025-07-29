package com.honlife.core.app.model.badge.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginEvent {

    /**
     * 로그인한 회원 ID
     */
    private Long memberId;
}
