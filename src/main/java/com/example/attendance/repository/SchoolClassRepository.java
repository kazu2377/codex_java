package com.example.attendance.repository;

import com.example.attendance.domain.SchoolClass;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByName(String name);

    @EntityGraph(attributePaths = {"subjects"})
    Optional<SchoolClass> findOneWithSubjectsById(Long id);

    @EntityGraph(attributePaths = {"subjects"})
    List<SchoolClass> findAllWithSubjects();
}
