package com.honlife.core.infra.payload;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageParam {
    @Min(1)
    private int page = 1;
    @Min(1)
    private int size = 4;
}
