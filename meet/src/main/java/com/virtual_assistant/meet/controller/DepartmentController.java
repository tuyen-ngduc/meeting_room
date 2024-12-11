package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @GetMapping("/allnames")
    public List<String> getAllDepartmentNames() {
        return departmentService.getAllDepartmentNames();
    }
}
