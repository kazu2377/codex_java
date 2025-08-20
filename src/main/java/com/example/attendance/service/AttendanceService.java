package com.example.attendance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.domain.Attendance;
import com.example.attendance.domain.AttendanceStatus;
import com.example.attendance.domain.Student;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.StudentRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    public List<Student> activeStudents() {
        return studentRepository.findAllByActiveTrueOrderByStudentNumberAsc();
    }

    public List<Student> activeStudents(String query) {
        if (query == null || query.isBlank()) {
            return activeStudents();
        }
        return studentRepository.searchActive(query.trim());
    }

    public List<Attendance> getAttendanceForDate(LocalDate date) {
        return attendanceRepository.findAllByDateOrderByStudent_StudentNumberAsc(date);
    }

    public Map<Long, Attendance> getAttendanceMapForDate(LocalDate date) {
        Map<Long, Attendance> map = new HashMap<>();
        for (Attendance a : getAttendanceForDate(date)) {
            map.put(a.getStudent().getId(), a);
        }
        return map;
    }

    @Transactional
    public void markAttendance(LocalDate date, Map<Long, AttendanceStatus> statuses, Map<Long, String> notes) {
        for (Map.Entry<Long, AttendanceStatus> e : statuses.entrySet()) {
            Long studentId = e.getKey();
            AttendanceStatus status = e.getValue();
            String note = Optional.ofNullable(notes.get(studentId)).orElse("");
            Student s = studentRepository.findById(studentId).orElse(null);
            if (s == null) continue;
            Attendance a = attendanceRepository.findByStudentAndDate(s, date)
                    .orElse(new Attendance(null, s, date, status, note));
            a.setStatus(status);
            a.setNote(note);
            attendanceRepository.save(a);
        }
    }

    public Optional<Student> findStudent(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student s) {
        return studentRepository.save(s);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
