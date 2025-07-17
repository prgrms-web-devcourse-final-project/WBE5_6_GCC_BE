package com.honlife.core.app.model.category.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterestCategoryServiceTest {

    @Autowired
    InterestCategoryService interestCategoryService;

    @Test
    void deleteByMemberId(){
        interestCategoryService.softDropInterestCategoryByMemberId(3L);
    }

}