package com.example.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.attendance.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByActiveTrueOrderByStudentNumberAsc();
    Optional<Student> findByStudentNumber(String studentNumber);

    @Query("select s from Student s where s.active = true and (lower(s.name) like lower(concat('%', :q, '%')) or lower(s.studentNumber) like lower(concat('%', :q, '%'))) order by s.studentNumber asc")
    List<Student> searchActive(@Param("q") String q);
}
