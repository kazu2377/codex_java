package com.example.attendance.controller;

import com.example.attendance.repository.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {
    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/people")
    public String list(Model model) {
        model.addAttribute("people", repository.findAll());
        return "people";
    }
}
