package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.dto.response.EmployeeDTO;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public List<EmployeeDTO> getAllEmployee() {
        return employeeRepository.findEmployee();
    }
}
