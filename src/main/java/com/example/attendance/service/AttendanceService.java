package com.example.attendance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.attendance.domain.Attendance;
import com.example.attendance.domain.Student;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.StudentRepository;

@Service
public class AttendanceService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(StudentRepository studentRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Student> listStudents() {
        return studentRepository.findAll();
    }

    public Page<Student> listStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public List<Attendance> getAttendanceForDate(LocalDate date) {
        return attendanceRepository.findAllByDateOrderByStudent_StudentNumberAsc(date);
    }

    public List<Attendance> getAttendanceForDate(LocalDate date, Long classId, Long subjectId) {
        if (classId != null && subjectId != null) {
            return attendanceRepository
                    .findAllByDateAndStudent_SchoolClass_IdAndSubject_IdOrderByStudent_StudentNumberAsc(date, classId,
                            subjectId);
        } else if (classId != null) {
            return attendanceRepository.findAllByDateAndStudent_SchoolClass_IdOrderByStudent_StudentNumberAsc(date,
                    classId);
        } else if (subjectId != null) {
            return attendanceRepository.findAllByDateAndSubject_IdOrderByStudent_StudentNumberAsc(date, subjectId);
        }
        return getAttendanceForDate(date);
    }

    public List<Student> activeStudents(Long classId, String query) {
        // TODO: filter by classId/query as needed
        return studentRepository.findAll();
    }

    public java.util.Map<Long, Attendance> getAttendanceMapForDate(LocalDate date) {
        java.util.Map<Long, Attendance> map = new java.util.HashMap<>();
        for (Attendance a : getAttendanceForDate(date)) {
            if (a.getStudent() != null) {
                map.put(a.getStudent().getId(), a);
            }
        }
        return map;
    }

    public void markAttendance(LocalDate date, Long subjectId,
            java.util.Map<Long, com.example.attendance.domain.AttendanceStatus> statuses,
            java.util.Map<Long, String> notes) {
        // No-op stub: implement persistence if needed
    }
}
