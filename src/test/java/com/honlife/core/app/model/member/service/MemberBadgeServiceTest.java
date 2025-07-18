package com.honlife.core.app.model.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MemberBadgeServiceTest {

    @Autowired
    MemberBadgeService memberBadgeService;

    @Test
    void deleteByMemberId(){
        memberBadgeService.softDropMemberBadgeByMemberId(2L);
    }

}