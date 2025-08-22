package com.example.attendance.controller;

import com.example.attendance.domain.Student;
import com.example.attendance.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final AttendanceService attendanceService;

    public StudentController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping
    public String list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "dir", defaultValue = "asc") String dir,
            Model model
    ) {
        String sortProp = switch (sort) {
            case "name", "studentNumber", "id" -> sort; // allowlisted
            default -> "id";
        };
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(size, 50), Sort.by(direction, sortProp));
        Page<Student> students = attendanceService.listStudents(pageable);

        model.addAttribute("page", students);
        model.addAttribute("sort", sortProp);
        model.addAttribute("dir", direction.isAscending() ? "asc" : "desc");
        model.addAttribute("toggleDir", direction.isAscending() ? "desc" : "asc");
        return "students/list";
    }
}
