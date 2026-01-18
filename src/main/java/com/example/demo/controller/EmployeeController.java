package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

  @Autowired
  private EmployeeService service;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("employees", service.getAll());
    return "index";
  }

  @GetMapping("/form")
  public String form(Model model) {
    model.addAttribute("employee", new Employee());
    return "form";
  }

  @PostMapping("/add")
  public String add(@ModelAttribute Employee emp) {
    service.add(emp);
    return "redirect:/";
  }

  // ✅ FIX: Explicitly specify path variable name
  @GetMapping("/delete/{id}")
  public String delete(@PathVariable("id") int id) {
    service.delete(id);
    return "redirect:/";
  }

  @ResponseBody
  @GetMapping("/api/employees")
  public List<Employee> apiEmployees() {
    return service.getAll();
  }
}
