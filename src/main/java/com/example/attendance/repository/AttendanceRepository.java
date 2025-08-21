package com.example.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.domain.Attendance;
import com.example.attendance.domain.Student;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @EntityGraph(attributePaths = {"student", "student.schoolClass", "subject"})
    List<Attendance> findAllByDateOrderByStudent_StudentNumberAsc(LocalDate date);
    Optional<Attendance> findByStudentAndDate(Student student, LocalDate date);
    List<Attendance> findAllByStudentOrderByDateDesc(Student student);

    @EntityGraph(attributePaths = {"student", "student.schoolClass", "subject"})
    List<Attendance> findAllByDateAndStudent_SchoolClass_IdOrderByStudent_StudentNumberAsc(LocalDate date, Long classId);

    @EntityGraph(attributePaths = {"student", "student.schoolClass", "subject"})
    List<Attendance> findAllByDateAndSubject_IdOrderByStudent_StudentNumberAsc(LocalDate date, Long subjectId);

    @EntityGraph(attributePaths = {"student", "student.schoolClass", "subject"})
    List<Attendance> findAllByDateAndStudent_SchoolClass_IdAndSubject_IdOrderByStudent_StudentNumberAsc(LocalDate date, Long classId, Long subjectId);
}
