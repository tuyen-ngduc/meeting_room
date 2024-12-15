package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Department;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Position;
import com.virtual_assistant.meet.dto.request.EmployeeDTO;


import com.virtual_assistant.meet.repository.DepartmentRepository;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import com.virtual_assistant.meet.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<com.virtual_assistant.meet.dto.response.EmployeeDTO> getAllEmployee() {
        return employeeRepository.findEmployee();
    }

    // Thêm nhân viên mới
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        // Kiểm tra xem nhân viên đã tồn tại chưa (dựa vào id nhân viên kiểu String)
        if (employeeRepository.existsById(employeeDTO.getId())) {
            throw new RuntimeException("Employee with ID " + employeeDTO.getId() + " already exists.");
        }

        Employee newEmployee = new Employee();
        newEmployee.setId(employeeDTO.getId());
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setBank(employeeDTO.getBank());
        newEmployee.setAddress(employeeDTO.getAddress());
        newEmployee.setPosition(positionRepository.findByName(employeeDTO.getPosition()).orElseThrow(()->new RuntimeException("Position not found")));
        newEmployee.setDepartment(departmentRepository.findByName(employeeDTO.getDepartment()).orElseThrow(()-> new RuntimeException("Department not found")));
        newEmployee.setDob(employeeDTO.getDob());
        newEmployee.setDegree(employeeDTO.getDegree());
        newEmployee.setIdentification(employeeDTO.getIdentification());
        newEmployee.setWorkplace(employeeDTO.getWorkplace());
        newEmployee.setPhoneNumber(employeeDTO.getPhoneNumber());
        newEmployee.setBankAccount(employeeDTO.getBankAccount());


        // Lưu nhân viên vào cơ sở dữ liệu
        return employeeRepository.save(newEmployee);
    }
    // Cập nhật nhân viên
    public Employee updateEmployee(EmployeeDTO employeeDTO) {
        // Kiểm tra xem nhân viên đã tồn tại chưa (dựa vào id nhân viên kiểu String)
        if (!employeeRepository.existsById(employeeDTO.getId())) {
            throw new RuntimeException("Employee with ID " + employeeDTO.getId() + " not already exists.");
        }

        Employee newEmployee = new Employee();
        newEmployee.setId(employeeDTO.getId());
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setBank(employeeDTO.getBank());
        newEmployee.setAddress(employeeDTO.getAddress());
        newEmployee.setPosition(positionRepository.findByName(employeeDTO.getPosition()).orElseThrow(()->new RuntimeException("Position not found")));
        newEmployee.setDepartment(departmentRepository.findByName(employeeDTO.getDepartment()).orElseThrow(()-> new RuntimeException("Department not found")));
        newEmployee.setDob(employeeDTO.getDob());
        newEmployee.setDegree(employeeDTO.getDegree());
        newEmployee.setIdentification(employeeDTO.getIdentification());
        newEmployee.setWorkplace(employeeDTO.getWorkplace());
        newEmployee.setPhoneNumber(employeeDTO.getPhoneNumber());
        newEmployee.setBankAccount(employeeDTO.getBankAccount());


        // Lưu nhân viên vào cơ sở dữ liệu
        return employeeRepository.save(newEmployee);
    }



    // Xóa nhân viên
    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);
    }

    // Tìm nhân viên theo id
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}

