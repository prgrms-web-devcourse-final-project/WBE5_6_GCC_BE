package com.honlife.core.app.model.member.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberItemServiceTest {

    @Autowired
    MemberItemService memberItemService;

    @Test
    void deleteMemberItemByMemberId() {
        memberItemService.softDropMemberItemByMemberId(2L);
    }

}