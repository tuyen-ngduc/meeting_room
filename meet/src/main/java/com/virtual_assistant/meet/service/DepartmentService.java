package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Department;
import com.virtual_assistant.meet.dto.request.DepartmentDTO;
import com.virtual_assistant.meet.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    public List<String> getAllDepartmentNames() {
        return departmentRepository.findAllDepartmentNames();
    }

    // Tạo mới phòng ban với mã phòng ban do người dùng cung cấp
    public Department createDepartment(String id, String name, String description) {
        // Kiểm tra nếu phòng ban với mã ID đã tồn tại hay chưa
        if (departmentRepository.existsById(id)) {
            throw new RuntimeException("Department with id " + id + " already exists");
        }

        Department department = new Department(id, name, description);
        return departmentRepository.save(department);
    }

    // Cập nhật phòng ban
    public Department updateDepartment(String id, String name, String description) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id " + id));

        department.setName(name);
        department.setDescription(description);
        return departmentRepository.save(department);
    }

    // Xóa phòng ban
    public void deleteDepartment(String id) {
        departmentRepository.deleteById(id);
    }



}
