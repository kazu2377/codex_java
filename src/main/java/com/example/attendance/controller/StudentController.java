package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.attendance.domain.SchoolClass;
import com.example.attendance.domain.Student;
import com.example.attendance.service.AttendanceService;
import com.example.attendance.repository.SchoolClassRepository;
import com.example.attendance.repository.StudentRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final AttendanceService service;
    private final SchoolClassRepository classRepository;
    private final StudentRepository studentRepository;

    public StudentController(AttendanceService service, SchoolClassRepository classRepository, StudentRepository studentRepository) {
        this.service = service;
        this.classRepository = classRepository;
        this.studentRepository = studentRepository;
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
        model.addAttribute("classes", classRepository.findAll());
        model.addAttribute("selectedClassId", null);
        return "students/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("student") Student student,
                         BindingResult bindingResult,
                         @RequestParam(name = "classId", required = false) Long classId,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("classes", classRepository.findAll());
            model.addAttribute("selectedClassId", classId);
            return "students/form";
        }
        student.setActive(true);
        if (classId != null) {
            SchoolClass clazz = classRepository.findById(classId).orElse(null);
            student.setSchoolClass(clazz);
        } else {
            student.setSchoolClass(null);
        }
        service.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Student s = studentRepository.findOneWithClassById(id).orElseThrow();
        Long selected = (s.getSchoolClass() != null) ? s.getSchoolClass().getId() : null;
        model.addAttribute("student", s);
        model.addAttribute("classes", classRepository.findAll());
        model.addAttribute("selectedClassId", selected);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("student") Student student,
                         BindingResult bindingResult,
                         @RequestParam(name = "classId", required = false) Long classId,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("classes", classRepository.findAll());
            model.addAttribute("selectedClassId", classId);
            return "students/form";
        }
        student.setId(id);
        if (classId != null) {
            SchoolClass clazz = classRepository.findById(classId).orElse(null);
            student.setSchoolClass(clazz);
        } else {
            student.setSchoolClass(null);
        }
        service.saveStudent(student);
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return "redirect:/students";
    }
}
