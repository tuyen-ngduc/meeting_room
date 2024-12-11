package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.dto.response.EmployeeDTO;
import com.virtual_assistant.meet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @GetMapping("/infor")
    public List<EmployeeDTO> getAllEmployee() {
       return employeeService.getAllEmployee();
    }
}
