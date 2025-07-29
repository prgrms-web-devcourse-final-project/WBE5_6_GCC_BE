package com.honlife.core.app.controller.badge.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeEquipRequest {

    @NotNull(message = "isEquipped 값은 필수입니다.")
    private Boolean isEquipped;
}
