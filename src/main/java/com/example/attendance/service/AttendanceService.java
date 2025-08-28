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

/**
 * 出席管理に関するサービスクラスです。
 * 学生や出席情報の取得・操作を行います。
 */
@Service
public class AttendanceService {
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * AttendanceServiceのコンストラクタ。
     * 
     * @param studentRepository    学生リポジトリ
     * @param attendanceRepository 出席リポジトリ
     */
    public AttendanceService(StudentRepository studentRepository, AttendanceRepository attendanceRepository) {
        this.studentRepository = studentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    /**
     * 全学生のリストを取得します。
     * 
     * @return 学生リスト
     */
    public List<Student> listStudents() {
        return studentRepository.findAll();
    }

    /**
     * ページングされた学生リストを取得します。
     * 
     * @param pageable ページ情報
     * @return ページングされた学生リスト
     */
    public Page<Student> listStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    /**
     * 名前または学籍番号で検索した学生のページングリストを取得します。
     * 
     * @param q        検索クエリ
     * @param pageable ページ情報
     * @return ページングされた学生リスト
     */
    public Page<Student> listStudents(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return listStudents(pageable);
        }
        String kw = q.trim();
        return studentRepository.findByNameContainingIgnoreCaseOrStudentNumberContainingIgnoreCase(kw, kw, pageable);
    }

    /**
     * クエリとクラスIDで学生を検索し、ページングリストを取得します。
     * 
     * @param q        検索クエリ
     * @param classId  クラスID
     * @param pageable ページ情報
     * @return ページングされた学生リスト
     */
    public Page<Student> listStudents(String q, Long classId, Pageable pageable) {
        boolean hasQuery = q != null && !q.isBlank();
        if (classId == null) {
            return listStudents(q, pageable);
        }
        if (!hasQuery) {
            return studentRepository.findBySchoolClass_Id(classId, pageable);
        }
        String kw = q.trim();
        return studentRepository
                .findBySchoolClass_IdAndNameContainingIgnoreCaseOrSchoolClass_IdAndStudentNumberContainingIgnoreCase(
                        classId, kw, classId, kw, pageable);
    }

    /**
     * 指定した日付の出席情報を取得します。
     * 
     * @param date 日付
     * @return 出席情報リスト
     */
    public List<Attendance> getAttendanceForDate(LocalDate date) {
        return attendanceRepository.findAllByDateOrderByStudent_StudentNumberAsc(date);
    }

    /**
     * 指定した日付・クラスID・科目IDの出席情報を取得します。
     * 
     * @param date      日付
     * @param classId   クラスID
     * @param subjectId 科目ID
     * @return 出席情報リスト
     */
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

    /**
     * アクティブな学生のリストを取得します。
     * 
     * @param classId クラスID
     * @param query   検索クエリ
     * @return 学生リスト
     */
    public List<Student> activeStudents(Long classId, String query) {
        // TODO: filter by classId/query as needed
        return studentRepository.findAll();
    }

    /**
     * 指定した日付の出席情報を学生IDをキーとしたマップで取得します。
     * 
     * @param date 日付
     * @return 学生IDをキーとした出席情報のマップ
     */
    public java.util.Map<Long, Attendance> getAttendanceMapForDate(LocalDate date) {
        java.util.Map<Long, Attendance> map = new java.util.HashMap<>();
        for (Attendance a : getAttendanceForDate(date)) {
            if (a.getStudent() != null) {
                map.put(a.getStudent().getId(), a);
            }
        }
        return map;
    }

    /**
     * 出席情報を記録します（現在はスタブ実装）。
     * 
     * @param date      日付
     * @param subjectId 科目ID
     * @param statuses  学生IDをキーとした出席ステータスのマップ
     * @param notes     学生IDをキーとした備考のマップ
     */
    public void markAttendance(LocalDate date, Long subjectId,
            java.util.Map<Long, com.example.attendance.domain.AttendanceStatus> statuses,
            java.util.Map<Long, String> notes) {
        // No-op stub: implement persistence if needed
    }
}
