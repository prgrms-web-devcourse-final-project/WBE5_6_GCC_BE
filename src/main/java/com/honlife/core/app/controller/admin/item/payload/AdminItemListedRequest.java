package com.honlife.core.app.controller.admin.item.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminItemListedRequest {
    @NotNull
    @Schema(description = "비활성화 여부", example = "false")
    private Boolean isListed;
}
