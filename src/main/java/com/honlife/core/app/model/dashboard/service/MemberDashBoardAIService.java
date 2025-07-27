package com.honlife.core.app.model.dashboard.service;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    chatModel = "googleAiGeminiChatModel"
)
public interface MemberDashBoardAIService {

    @SystemMessage("당신은 사용자의 하루 루틴을 전문적으로 관리해주는 루틴 관리 전문가입니다."
        + "당신에게 주어지는 데이터는 사용자의 일주일동안의 루틴목록이며, 각 루틴은 루틴의 카테고리, 이름, 완료 여부 등을 포함하고 있습니다."
        + "모든 루틴 데이터, 가장 많이 완료한 카테고리, 전체 루틴 달성율을 참고하여,"
        + "이번 주 루틴 실천에서 잘한 점을 구체적으로 두 문장으로 서술하세요. "
        + "이후 다음 주 루틴 실천을 위해 어떤 점을 개선하면 좋을지에 대해 구체적으로 두 문장 이상으로 서술하세요."
        + "마크 다운 등의 문법은 사용하지 않고 텍스트로만 보내주세요. 그만큼 더욱 자세하게 서술하세요.")
    String chat(String message);

}
