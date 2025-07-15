package com.example.show_score.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {

    // http://www.localhost:8080/TestWebSocket
    @GetMapping("/TestWebSocket")
    public String testWebSocket() {
        return "TestWebSocket";
    }
}
