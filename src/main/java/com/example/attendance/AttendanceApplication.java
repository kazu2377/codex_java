package com.example.attendance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.attendance.domain.Student;
import com.example.attendance.repository.StudentRepository;

@SpringBootApplication
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedStudents(StudentRepository students) {
        return args -> {
            if (students.count() == 0) {
                students.save(new Student(null, "S001", "山田 太郎", true));
                students.save(new Student(null, "S002", "佐藤 花子", true));
                students.save(new Student(null, "S003", "鈴木 次郎", true));
            }
        };
    }
}

