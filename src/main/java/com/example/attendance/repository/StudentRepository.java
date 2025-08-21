package com.example.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.attendance.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @EntityGraph(attributePaths = { "schoolClass" })
    List<Student> findAllByActiveTrueOrderByStudentNumberAsc();

    @EntityGraph(attributePaths = { "schoolClass" })
    Page<Student> findAllByActiveTrue(Pageable pageable);

    Optional<Student> findByStudentNumber(String studentNumber);

    @EntityGraph(attributePaths = { "schoolClass" })
    @Query("select s from Student s where s.active = true and (lower(s.name) like lower(concat('%', :q, '%')) or lower(s.studentNumber) like lower(concat('%', :q, '%'))) order by s.studentNumber asc")
    List<Student> searchActive(@Param("q") String q);

    @EntityGraph(attributePaths = { "schoolClass" })
    @Query("select s from Student s where s.active = true and (lower(s.name) like lower(concat('%', :q, '%')) or lower(s.studentNumber) like lower(concat('%', :q, '%'))) ")
    Page<Student> searchActive(@Param("q") String q, Pageable pageable);

    @EntityGraph(attributePaths = { "schoolClass" })
    List<Student> findAllByActiveTrueAndSchoolClass_IdOrderByStudentNumberAsc(Long classId);

    @EntityGraph(attributePaths = { "schoolClass" })
    Page<Student> findAllByActiveTrueAndSchoolClass_Id(Long classId, Pageable pageable);

    @EntityGraph(attributePaths = { "schoolClass" })
    @Query("select s from Student s where s.active = true and s.schoolClass.id = :classId and (lower(s.name) like lower(concat('%', :q, '%')) or lower(s.studentNumber) like lower(concat('%', :q, '%'))) order by s.studentNumber asc")
    List<Student> searchActiveByClass(@Param("classId") Long classId, @Param("q") String q);

    @EntityGraph(attributePaths = { "schoolClass" })
    @Query("select s from Student s where s.active = true and s.schoolClass.id = :classId and (lower(s.name) like lower(concat('%', :q, '%')) or lower(s.studentNumber) like lower(concat('%', :q, '%'))) ")
    Page<Student> searchActiveByClass(@Param("classId") Long classId, @Param("q") String q, Pageable pageable);

    @EntityGraph(attributePaths = { "schoolClass" })
    Optional<Student> findOneWithClassById(Long id);
}
