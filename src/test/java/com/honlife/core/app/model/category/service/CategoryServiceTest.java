package com.honlife.core.app.model.category.service;

import static org.junit.jupiter.api.Assertions.*;

import com.honlife.core.app.model.routine.service.RoutineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void deleteRoutineWithMemberIdTest(){
        categoryService.deleteCategoryByMemberId(2L);
    }

}