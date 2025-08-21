package com.example.attendance.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.domain.SchoolClass;
import com.example.attendance.domain.Subject;
import com.example.attendance.repository.SchoolClassRepository;
import com.example.attendance.repository.SubjectRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/classes")
public class ClassController {

    private final SchoolClassRepository classRepository;
    private final SubjectRepository subjectRepository;

    public ClassController(SchoolClassRepository classRepository, SubjectRepository subjectRepository) {
        this.classRepository = classRepository;
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", classRepository.findAll());
        return "classes/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("clazz", new SchoolClass());
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("selectedSubjectIds", List.of());
        return "classes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("clazz") SchoolClass clazz,
            BindingResult bindingResult,
            @RequestParam(value = "subjectIds", required = false) List<Long> subjectIds,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("selectedSubjectIds", subjectIds == null ? List.of() : subjectIds);
            return "classes/form";
        }
        Set<Subject> subjects = new HashSet<>();
        if (subjectIds != null) {
            subjects.addAll(subjectRepository.findAllById(subjectIds));
        }
        clazz.setSubjects(subjects);
        classRepository.save(clazz);
        return "redirect:/classes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        SchoolClass clazz = classRepository.findOneWithSubjectsById(id).orElseThrow();
        model.addAttribute("clazz", clazz);
        model.addAttribute("allSubjects", subjectRepository.findAll());
        model.addAttribute("selectedSubjectIds", clazz.getSubjects().stream().map(Subject::getId).toList());
        return "classes/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("clazz") SchoolClass clazz,
            BindingResult bindingResult,
            @RequestParam(value = "subjectIds", required = false) List<Long> subjectIds,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allSubjects", subjectRepository.findAll());
            model.addAttribute("selectedSubjectIds", subjectIds == null ? List.of() : subjectIds);
            return "classes/form";
        }
        clazz.setId(id);
        Set<Subject> subjects = new HashSet<>();
        if (subjectIds != null) {
            subjects.addAll(subjectRepository.findAllById(subjectIds));
        }
        clazz.setSubjects(subjects);
        classRepository.save(clazz);
        return "redirect:/classes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        classRepository.deleteById(id);
        return "redirect:/classes";
    }
}
