package com.stakeit.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class apitestcontroller {
    @GetMapping("/test")
    public String test() {
        return "Stakeit API works";
    }
}
