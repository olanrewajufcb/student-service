package com.emis.studentsservice;@RestController
@RequestMapping("/api/v1/students")
public class TestController {

    @GetMapping("/ping")
    public Mono<String> ping() {
        return Mono.just("student-service-alive");
    }

}