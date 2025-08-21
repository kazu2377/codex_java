package com.example.attendance.controller;

import com.example.attendance.domain.Subject;
import com.example.attendance.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("subjects", subjectRepository.findAll());
        return "subjects/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("subject", new Subject());
        return "subjects/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("subject") Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "subjects/form";
        }
        subjectRepository.save(subject);
        return "redirect:/subjects";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Subject s = subjectRepository.findById(id).orElseThrow();
        model.addAttribute("subject", s);
        return "subjects/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("subject") Subject subject, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "subjects/form";
        }
        subject.setId(id);
        subjectRepository.save(subject);
        return "redirect:/subjects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        subjectRepository.deleteById(id);
        return "redirect:/subjects";
    }
}

