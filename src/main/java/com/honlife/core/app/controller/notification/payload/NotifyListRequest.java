package com.honlife.core.app.controller.notification.payload;

import com.honlife.core.app.model.notification.code.NotificationType;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotifyListRequest {

  @NotNull
  private NotificationType type;

}
