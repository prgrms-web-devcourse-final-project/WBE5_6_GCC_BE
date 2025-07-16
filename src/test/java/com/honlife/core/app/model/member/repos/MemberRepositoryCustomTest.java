package com.honlife.core.app.model.member.repos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryCustomTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void isEmailVerified() {

        System.out.println(memberRepository.isEmailVerified("user01@test.com"));
    }
}