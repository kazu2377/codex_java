package com.example.attendance.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.attendance.domain.AttendanceStatus;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.repository.SchoolClassRepository;
import com.example.attendance.repository.SubjectRepository;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService service;
    private final SchoolClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_DATE;

    public AttendanceController(AttendanceService service, SchoolClassRepository classRepository, SubjectRepository subjectRepository) {
        this.service = service;
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/mark")
    public String markForm(@RequestParam(name = "date", required = false)
                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam(name = "q", required = false) String q,
                           @RequestParam(name = "classId", required = false) Long classId,
                           @RequestParam(name = "subjectId", required = false) Long subjectId,
                           Model model) {
        LocalDate target = date != null ? date : LocalDate.now();
        model.addAttribute("date", target.format(FMT));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("students", service.activeStudents(classId, q));
        model.addAttribute("existing", service.getAttendanceMapForDate(target));
        model.addAttribute("statuses", EnumSet.allOf(AttendanceStatus.class));
        model.addAttribute("classes", classRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        // classId/subjectId をそのまま返してフォーム選択状態に使う
        model.addAttribute("classId", classId);
        model.addAttribute("subjectId", subjectId);
        return "attendance/mark";
    }

    @PostMapping("/mark")
    public String markSubmit(@RequestParam("date") String dateStr,
                             @RequestParam(name = "subjectId", required = false) Long subjectId,
                             @RequestParam(name = "classId", required = false) Long classId,
                             @RequestParam Map<String, String> params) {
        LocalDate date = LocalDate.parse(dateStr, FMT);
        Map<Long, AttendanceStatus> statuses = new HashMap<>();
        Map<Long, String> notes = new HashMap<>();

        // Expect params like status_1=PRESENT, note_1=...
        params.forEach((k, v) -> {
            if (k.startsWith("status_")) {
                Long id = Long.valueOf(k.substring("status_".length()));
                statuses.put(id, AttendanceStatus.valueOf(v));
            } else if (k.startsWith("note_")) {
                Long id = Long.valueOf(k.substring("note_".length()));
                notes.put(id, v);
            }
        });
        service.markAttendance(date, subjectId, statuses, notes);
        String redirect = "/attendance/report?date=" + date.format(FMT);
        if (subjectId != null) redirect += "&subjectId=" + subjectId;
        if (classId != null) redirect += "&classId=" + classId;
        return "redirect:" + redirect;
    }

    @GetMapping("/report")
    public String report(@RequestParam(name = "date", required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                         @RequestParam(name = "q", required = false) String q,
                         @RequestParam(name = "classId", required = false) Long classId,
                         @RequestParam(name = "subjectId", required = false) Long subjectId,
                         @RequestParam(name = "status", required = false) String statusParam,
                         Model model) {
        LocalDate target = date != null ? date : LocalDate.now();
        var list = service.getAttendanceForDate(target, classId, subjectId);
        String query = q == null ? "" : q.trim().toLowerCase();
        if (!query.isEmpty()) {
            list = list.stream().filter(r -> {
                var sn = r.getStudent().getStudentNumber().toLowerCase();
                var nm = r.getStudent().getName().toLowerCase();
                return sn.contains(query) || nm.contains(query);
            }).toList();
        }
        final AttendanceStatus filterStatus;
        if (statusParam != null && !statusParam.isBlank()) {
            AttendanceStatus tmp = null;
            try { tmp = AttendanceStatus.valueOf(statusParam); } catch (Exception ignored) {}
            filterStatus = tmp;
        } else {
            filterStatus = null;
        }
        if (filterStatus != null) {
            list = list.stream().filter(r -> r.getStatus() == filterStatus).toList();
        }
        model.addAttribute("date", target.format(FMT));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("status", filterStatus);
        model.addAttribute("records", list);
        model.addAttribute("statuses", EnumSet.allOf(AttendanceStatus.class));
        model.addAttribute("classes", classRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("classId", classId);
        model.addAttribute("subjectId", subjectId);
        return "attendance/report";
    }
}
