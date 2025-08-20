package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.domain.Attendance;
import com.example.attendance.domain.Student;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByDateOrderByStudent_StudentNumberAsc(LocalDate date);
    Optional<Attendance> findByStudentAndDate(Student student, LocalDate date);
    List<Attendance> findAllByStudentOrderByDateDesc(Student student);
}

