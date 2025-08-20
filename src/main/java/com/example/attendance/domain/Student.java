package com.example.attendance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_number", columnNames = {"student_number"})
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_number", nullable = false, length = 50)
    @NotBlank
    private String studentNumber;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String name;

    @Column(nullable = false)
    private boolean active = true;

    public Student() {
    }

    public Student(Long id, String studentNumber, String name, boolean active) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.name = name;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

