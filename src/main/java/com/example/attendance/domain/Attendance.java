package com.example.attendance.domain;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(name = "uk_attendance_student_date", columnNames = {"student_id", "date"})
})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Column(length = 255)
    private String note;

    public Attendance() {
    }

    public Attendance(Long id, Student student, LocalDate date, AttendanceStatus status, String note) {
        this.id = id;
        this.student = student;
        this.date = date;
        this.status = status;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

