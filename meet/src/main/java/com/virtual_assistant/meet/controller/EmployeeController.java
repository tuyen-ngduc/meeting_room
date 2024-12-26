package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.dto.request.UpdateProfileRequest;
import com.virtual_assistant.meet.dto.response.EmployeeDTO;
import com.virtual_assistant.meet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateEmployeeProfile(@RequestBody UpdateProfileRequest updateRequest) {
        try {
            employeeService.updateEmployeeProfile(updateRequest);
            return ResponseEntity.ok("Cập nhật thông tin thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/infor")
    public List<EmployeeDTO> getAllEmployee() {
       return employeeService.getAllEmployee();
    }


    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody com.virtual_assistant.meet.dto.request.EmployeeDTO employeeDTO) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employeeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateEmployee(@RequestBody com.virtual_assistant.meet.dto.request.EmployeeDTO employeDTO) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(employeDTO);
            return ResponseEntity.ok("Cập nhật nhân viên thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Employee deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting employee");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        try {
            Employee employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
