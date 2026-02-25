package com.emis.studentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentsServiceApplication.class, args);
    }

}
