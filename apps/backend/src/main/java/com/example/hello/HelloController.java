package com.example.hello;

import java.time.Instant;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final String environment;

    public HelloController(@Value("${app.environment:local}") String environment) {
        this.environment = environment;
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of(
                "message", "Hello World from Spring Boot",
                "environment", environment,
                "timestamp", Instant.now().toString());
    }
}
