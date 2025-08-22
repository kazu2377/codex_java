package com.example.attendance;

import com.example.attendance.domain.Student;
import com.example.attendance.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;
import java.util.Random;
import java.util.stream.IntStream;

@SpringBootApplication
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }

    // Simple dev seed so the list page has data to paginate/sort
    @Bean
    CommandLineRunner seedStudents(StudentRepository repo) {
        return args -> {
            if (repo.count() > 0) return;
            Random r = new Random(42);
            IntStream.rangeClosed(1, 50).forEach(i -> {
                String name = "学生 " + i;
                String sn = String.format(Locale.ROOT, "S%04d", i);
                repo.save(new Student(null, sn, name, true));
            });
        };
    }
}
