package com.kangwon.gowithusedemo.openAi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("/result")
    public String result() {
        return "/result";
    }
}
