package com.honlife.core.app.model.withdraw.code;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum WithdrawType {
    @Schema(description = "기록하는 게 귀찮아요")
    LAZY("기록하는 게 귀찮아요"),
    @Schema(description = "루틴이 잘 안 맞았어요")
    ROUTINE_NOT_FIT("루틴이 잘 안 맞았어요"),
    APP_DIFFICULTY("앱이 어렵거나 불편했어요"),
    LACK_OF_FEATURE("기대했던 기능이 없어요"),
    USING_OTHER_APP("이미 비슷한 앱을 쓰고 있어요"),
    NO_MOTIVATION("혼자 하니까 동기부여가 안 됐어요"),
    ETC("직접 입력");

    // enum이 값을 갖는 한글 설명을 저장하기 위한
    // 필드(맴버 변수) 선언
    private final String label;

    WithdrawType(String label) {
        this.label = label;
    }

}
