package com.example.attendance;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.attendance.model.Person;
import com.example.attendance.repository.PersonRepository;

import java.util.List;
import java.time.LocalDate;

import com.example.attendance.domain.*;
import com.example.attendance.repository.*;

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

    @Bean
    CommandLineRunner seedDomain(
            SubjectRepository subjectRepository,
            SchoolClassRepository classRepository,
            StudentRepository studentRepository,
            AttendanceRepository attendanceRepository
    ) {
        return args -> {
            // Seed Subjects
            if (subjectRepository.count() == 0) {
                subjectRepository.saveAll(List.of(
                        new Subject(null, "MATH", "Mathematics"),
                        new Subject(null, "ENG", "English"),
                        new Subject(null, "SCI", "Science")
                ));
            }

            // Seed Classes and link Subjects
            if (classRepository.count() == 0) {
                SchoolClass a = new SchoolClass(null, "Class A");
                SchoolClass b = new SchoolClass(null, "Class B");
                a = classRepository.save(a);
                b = classRepository.save(b);

                Subject math = subjectRepository.findByCode("MATH").orElseThrow();
                Subject eng = subjectRepository.findByCode("ENG").orElseThrow();
                Subject sci = subjectRepository.findByCode("SCI").orElseThrow();

                a.getSubjects().add(math);
                a.getSubjects().add(eng);
                b.getSubjects().add(math);
                b.getSubjects().add(sci);
                classRepository.saveAll(List.of(a, b));
            }

            // Seed Students and link to Class
            if (studentRepository.count() == 0) {
                SchoolClass a = classRepository.findByName("Class A").orElseThrow();
                SchoolClass b = classRepository.findByName("Class B").orElseThrow();
                Student s1 = new Student(null, "S001", "Alice", true);
                Student s2 = new Student(null, "S002", "Bob", true);
                Student s3 = new Student(null, "S003", "Charlie", true);
                Student s4 = new Student(null, "S004", "Diana", true);
                s1.setSchoolClass(a);
                s2.setSchoolClass(a);
                s3.setSchoolClass(b);
                s4.setSchoolClass(b);
                studentRepository.saveAll(List.of(s1, s2, s3, s4));
            }

            // Seed Attendance for today if empty
            if (attendanceRepository.count() == 0) {
                LocalDate today = LocalDate.now();
                Subject math = subjectRepository.findByCode("MATH").orElse(null);
                for (Student s : studentRepository.findAll()) {
                    Attendance a = new Attendance(null, s, today, AttendanceStatus.PRESENT, "");
                    a.setSubject(math);
                    attendanceRepository.save(a);
                }
            }
        };
    }
}
