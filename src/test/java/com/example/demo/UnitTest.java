package com.example.demo;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
  @Test
  void testAddEmployee() {
    EmployeeService service = new EmployeeService();
    Employee emp = new Employee(1, "Alice", "Dev");
    service.add(emp);
    assertEquals(1, service.getAll().size());
  }
}
