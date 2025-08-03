package com.honlife.core.app.model.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService<T> {

   SseEmitter connect(Long memberId);

   void sendTo(Long memberId);

}
