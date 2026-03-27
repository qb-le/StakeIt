package com.stakeit.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class apitestcontroller {
    @GetMapping()
    public String test() {
        return
            """
            Stakeit API works
            <a href="http://145.220.72.157:8081/actuator/health/db" target="_blank">
                Check DB Health
            </a>
            """;
    }
}
