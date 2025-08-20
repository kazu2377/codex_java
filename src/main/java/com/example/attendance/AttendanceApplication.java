package com.example.attendance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.attendance.model.Person;
import com.example.attendance.repository.PersonRepository;

import java.util.List;

@SpringBootApplication
public class AttendanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(PersonRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.saveAll(List.of(
                        new Person(null, "Taro", "taro@example.com"),
                        new Person(null, "Hanako", "hanako@example.com"),
                        new Person(null, "Jiro", "jiro@example.com")
                ));
            }
        };
    }
}

