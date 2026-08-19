package com.example.demo.service;

import com.example.demo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private final Map<Integer, Employee> employees = new HashMap<>();

    public List<Employee> getAll() {
        return new ArrayList<>(employees.values());
    }

    public Employee getById(int id) {
        return employees.get(id);
    }

    public Employee add(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    public Employee update(int id, Employee employee) {

        if (!employees.containsKey(id)) {
            return null;
        }

        employee.setId(id);
        employees.put(id, employee);

        return employee;
    }

    public boolean delete(int id) {

        if (!employees.containsKey(id)) {
            return false;
        }

        employees.remove(id);
        return true;
    }
}
