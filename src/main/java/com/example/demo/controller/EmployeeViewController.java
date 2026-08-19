package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeViewController {

    private final EmployeeService service;

    public EmployeeViewController(EmployeeService service) {
        this.service = service;
    }

    // Home page
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", service.getAll());
        return "index";
    }

    // Add employee form
    @GetMapping("/form")
    public String form(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("editMode", false);
        return "form";
    }

    // Edit employee form
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {

        Employee employee = service.getById(id);

        if (employee == null) {
            return "redirect:/";
        }

        model.addAttribute("employee", employee);
        model.addAttribute("editMode", true);

        return "form";
    }

    // Add employee
    @PostMapping("/add")
    public String add(@ModelAttribute Employee employee) {
        service.add(employee);
        return "redirect:/";
    }

    // Update employee
    @PostMapping("/update/{id}")
    public String update(
            @PathVariable("id") int id,
            @ModelAttribute Employee employee) {

        service.update(id, employee);

        return "redirect:/";
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        service.delete(id);
        return "redirect:/";
    }
}
