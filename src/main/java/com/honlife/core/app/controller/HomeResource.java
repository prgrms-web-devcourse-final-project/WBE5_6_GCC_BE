package com.honlife.core.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeResource {

    @GetMapping("/")
    public String index() {
//        return "\"Hello World!\"";
        return "<a href=\"/oauth2/authorization/google\">구글 로그인</a>";
    }

}
