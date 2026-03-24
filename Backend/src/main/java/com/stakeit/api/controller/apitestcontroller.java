package com.stakeit.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class apitestcontroller {
    @GetMapping()
    public String test() {
        return "Stakeit API works";

    }
}
