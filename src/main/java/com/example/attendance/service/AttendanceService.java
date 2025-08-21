package com.example.attendance.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.domain.*;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.SchoolClassRepository;
import com.example.attendance.repository.SubjectRepository;
import com.example.attendance.repository.StudentRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository,
                             SchoolClassRepository classRepository, SubjectRepository subjectRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
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

    public List<Student> activeStudents(Long classId, String query) {
        boolean hasQuery = query != null && !query.isBlank();
        if (classId == null) {
            return hasQuery ? studentRepository.searchActive(query.trim())
                    : studentRepository.findAllByActiveTrueOrderByStudentNumberAsc();
        }
        return hasQuery ? studentRepository.searchActiveByClass(classId, query.trim())
                : studentRepository.findAllByActiveTrueAndSchoolClass_IdOrderByStudentNumberAsc(classId);
    }

    public List<Attendance> getAttendanceForDate(LocalDate date) {
        return attendanceRepository.findAllByDateOrderByStudent_StudentNumberAsc(date);
    }

    public List<Attendance> getAttendanceForDate(LocalDate date, Long classId, Long subjectId) {
        if (classId != null && subjectId != null) {
            return attendanceRepository.findAllByDateAndStudent_SchoolClass_IdAndSubject_IdOrderByStudent_StudentNumberAsc(date, classId, subjectId);
        } else if (classId != null) {
            return attendanceRepository.findAllByDateAndStudent_SchoolClass_IdOrderByStudent_StudentNumberAsc(date, classId);
        } else if (subjectId != null) {
            return attendanceRepository.findAllByDateAndSubject_IdOrderByStudent_StudentNumberAsc(date, subjectId);
        }
        return getAttendanceForDate(date);
    }

    public Map<Long, Attendance> getAttendanceMapForDate(LocalDate date) {
        Map<Long, Attendance> map = new HashMap<>();
        for (Attendance a : getAttendanceForDate(date)) {
            map.put(a.getStudent().getId(), a);
        }
        return map;
    }

    @Transactional
    public void markAttendance(LocalDate date, Long subjectId, Map<Long, AttendanceStatus> statuses, Map<Long, String> notes) {
        final Subject subject = subjectId != null ? subjectRepository.findById(subjectId).orElse(null) : null;
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
            a.setSubject(subject);
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
