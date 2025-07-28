package com.honlife.core.app.model.badge.code;

public enum BadgeStatus {

    LOCKED,      // 미달성 (진행률 표시: 45/100)
    ACHIEVABLE,  // 달성 완료, 미획득 ("획득하기" 버튼)
    OWNED,       // 획득 완료, 미장착 ("장착하기" 버튼)
    EQUIPPED     // 장착 중 ("착용 해제" 버튼)
    
}
