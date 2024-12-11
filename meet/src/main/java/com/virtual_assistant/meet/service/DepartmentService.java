package com.virtual_assistant.meet.service;

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

}
