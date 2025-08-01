package com.honlife.core.app.model.withdraw.code;

public enum WithdrawType {

    TOO_MUCH_EFFORT("기록하는 게 귀찮아요"),
    ROUTINE_MISMATCH("루틴이 잘 안 맞았어요"),
    UX_ISSUE("앱이 어렵거나 불편했어요"),
    MISSING_FEATURE("기대했던 기능이 없어요"),
    USING_OTHER_APP("이미 비슷한 앱을 쓰고 있어요"),
    NO_MOTIVATION("혼자 하니까 동기 부여가 안 됐어요"),
    ETC("직접 입력");

    private final String description;

    WithdrawType(String description) {
        this.description = description;
    }

}
