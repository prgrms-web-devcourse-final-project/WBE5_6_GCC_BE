package com.honlife.core.app.model.dashboard.service;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    chatModel = "googleAiGeminiChatModel"
)
public interface MemberDashBoardAIService {

    @SystemMessage("당신은 루틴 관리 앱의 친절하고 동기 부여적인 AI 코치입니다.\n"
        + "사용자의 이번 주 루틴 데이터를 기반으로, 이번 주에 잘한 점을 두 문장 이상으로 칭찬해주세요. "
        + "다음 주에 개선하면 좋을 점을 긍정적으로 제안하는 두 문장 이상의 AI 코멘트를 작성해주세요.\n")
    String chat(String message);

}
