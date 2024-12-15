package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Department;
import com.virtual_assistant.meet.dto.request.DepartmentDTO;
import com.virtual_assistant.meet.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    @GetMapping("/allnames")
    public List<String> getAllDepartmentNames() {
        return departmentService.getAllDepartmentNames();
    }

    // Thêm mới phòng ban
    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        try {
            Department department = departmentService.createDepartment(departmentDTO.getId(), departmentDTO.getName(), departmentDTO.getDescription());
            return ResponseEntity.ok("Department created successfully with ID: " + department.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cập nhật phòng ban
    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable String id, @RequestBody DepartmentDTO departmentDTO) {
        try {
            Department department = departmentService.updateDepartment(id, departmentDTO.getName(), departmentDTO.getDescription());
            return ResponseEntity.ok("Department updated successfully with ID: " + department.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa phòng ban
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable String id) {
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.ok("Department deleted successfully with ID: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
