package com.honlife.core.app.model.notification.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseServiceImpl implements SseService{

  //모든 사용자 이곳에 저장된다
  private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final NotifyListService notifyListService;


  @Override
  public SseEmitter connect(Long memberId) {
    SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

    //클라이언트 연결 종료한다면 자동으로 emitter 제거
    emitter.onCompletion(() -> emitters.remove(memberId));
    emitter.onTimeout(() -> {
      emitter.complete();
      emitters.remove(memberId);
      log.info("성공");
    });
    //에러발생시 emitter 종료 후 제거한다
    emitter.onError(e -> {
      emitter.completeWithError(e);
      emitters.remove(memberId);
      log.info("에러");
    });


    emitters.put(memberId, emitter);
    boolean hasUnread = notifyListService.hasUnread(memberId);
    if(hasUnread) {
      new Thread(() -> {
        try {
          Thread.sleep(500);
          sendTo(memberId);
        } catch (InterruptedException ignored) {
        }
      }).start();
    }

    return emitter;
  }

  /**
   * 클라이언트에게 알람 전송
   * @param memberId
   */
  @Override
  public void sendTo(Long memberId){

    SseEmitter emitter = emitters.get(memberId);
    //연결 안되있으면 리턴
    if(emitter == null){
      return;
    }
    boolean hasUnread = notifyListService.hasUnread(memberId);
    try {
      emitter.send(SseEmitter.event()
          .name("notify") //이 이름으로 SSE이벤트를 보낸다
          .data(hasUnread)
      );
    }catch (IOException e){
        emitter.completeWithError(e);
        emitters.remove(memberId);

    }

    log.info("[SSE Success] memberId={}", memberId);


  }

}
