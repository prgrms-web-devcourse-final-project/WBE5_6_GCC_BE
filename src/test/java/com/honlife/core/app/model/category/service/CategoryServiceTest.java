package com.honlife.core.app.model.category.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void deleteRoutineWithMemberIdTest(){
        categoryService.softDropCategoryByMemberId(2L);
    }

}