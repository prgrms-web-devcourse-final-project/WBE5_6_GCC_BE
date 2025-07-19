package com.honlife.core.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeResource {

    @GetMapping("/")
    public String index() {
        return "redirect:/swagger-ui/index.html";
    }

}
