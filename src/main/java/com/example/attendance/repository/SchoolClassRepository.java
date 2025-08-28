package com.example.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.example.attendance.domain.SchoolClass;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    Optional<SchoolClass> findByName(String name);

    @EntityGraph(attributePaths = { "subjects" })
    Optional<SchoolClass> findOneWithSubjectsById(Long id);

    @EntityGraph(attributePaths = { "subjects" })
    @Override
    @NonNull
    List<SchoolClass> findAll();
}
