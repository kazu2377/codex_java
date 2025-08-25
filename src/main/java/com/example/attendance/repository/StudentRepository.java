package com.example.attendance.repository;

import com.example.attendance.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByNameContainingIgnoreCaseOrStudentNumberContainingIgnoreCase(
            String name, String studentNumber, Pageable pageable);

    Page<Student> findBySchoolClass_Id(Long classId, Pageable pageable);

    Page<Student> findBySchoolClass_IdAndNameContainingIgnoreCaseOrSchoolClass_IdAndStudentNumberContainingIgnoreCase(
            Long classId1, String name,
            Long classId2, String studentNumber,
            Pageable pageable);
}
