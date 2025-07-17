package com.honlife.core.app.model.routine.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class RoutineServiceTest {

    @Autowired
    private RoutineService routineService;

    @Test
    void deleteRoutineWithMemberIdTest(){
        routineService.softDropRoutineByMemberId(2L);
    }

}