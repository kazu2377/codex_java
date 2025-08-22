package com.example.attendance.repository;

import com.example.attendance.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingIgnoreCaseOrStudentNumberContainingIgnoreCase(
            String name, String studentNumber, Pageable pageable);
}
