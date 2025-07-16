package com.honlife.core.app.model.category.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterestCategoryServiceTest {

    @Autowired
    InterestCategoryService interestCategoryService;

    @Test
    void deleteByMemberId(){
        interestCategoryService.deleteInterestCategoryByMemberId(3L);
    }

}