package com.example.demo.service;

import com.example.demo.model.Employee;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmployeeService {
  private final Map<Integer, Employee> employees = new HashMap<>();

  public List<Employee> getAll() {
    return new ArrayList<>(employees.values());
  }

  public Employee getById(int id) {
    return employees.get(id);
  }

  public void add(Employee emp) {
    employees.put(emp.getId(), emp);
  }

  public void update(int id, Employee emp) {
    employees.put(id, emp);
  }

  public void delete(int id) {
    employees.remove(id);
  }
}
