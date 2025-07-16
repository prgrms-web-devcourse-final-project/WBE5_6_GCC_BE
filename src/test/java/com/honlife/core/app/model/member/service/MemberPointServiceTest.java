package com.honlife.core.app.model.member.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberPointServiceTest {

    @Autowired
    MemberPointService memberPointService;

    @Test
    void deleteMemberPointByMemberId() {
        memberPointService.deleteMemberPointByMemberId(2L);
    }
}