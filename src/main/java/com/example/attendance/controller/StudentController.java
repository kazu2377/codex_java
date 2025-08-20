package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.attendance.domain.Student;
import com.example.attendance.service.AttendanceService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final AttendanceService service;

    public StudentController(AttendanceService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(name = "q", required = false) String q, Model model) {
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("students", service.activeStudents(q));
        return "students/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("student") Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "students/form";
        }
        student.setActive(true);
        service.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student s = service.findStudent(id).orElseThrow();
        model.addAttribute("student", s);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("student") Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "students/form";
        }
        student.setId(id);
        service.saveStudent(student);
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return "redirect:/students";
    }
}
