package com.honlife.core.app.controller.category.payload;


import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateInterestCategoryRequest {

    @NotEmpty
    private List<Long> interestedCategoryIds;

}
