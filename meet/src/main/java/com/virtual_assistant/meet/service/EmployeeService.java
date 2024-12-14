package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Department;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Position;
import com.virtual_assistant.meet.dto.response.EmployeeDTO;
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

    public List<EmployeeDTO> getAllEmployee() {
        return employeeRepository.findEmployee();
    }

    // Thêm nhân viên mới
    public Employee createEmployee(Employee employee) {
        // Kiểm tra xem nhân viên đã tồn tại chưa (dựa vào id nhân viên kiểu String)
        if (employeeRepository.existsById(employee.getId())) {
            throw new RuntimeException("Employee with ID " + employee.getId() + " already exists.");
        }
        // Kiểm tra và thiết lập Position nếu có
        if (employee.getPosition() != null && employee.getPosition().getId() != null) {
            Position position = positionRepository.findById(employee.getPosition().getId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            employee.setPosition(position);
        }

        // Kiểm tra và thiết lập Department nếu có
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }

        // Lưu nhân viên vào cơ sở dữ liệu
        return employeeRepository.save(employee);
    }

    // Cập nhật thông tin nhân viên
    public Employee updateEmployee(String id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Cập nhật các trường
        employee.setName(updatedEmployee.getName());
        employee.setDob(updatedEmployee.getDob());
        employee.setPhoneNumber(updatedEmployee.getPhoneNumber());
        employee.setDegree(updatedEmployee.getDegree());
        employee.setIdentification(updatedEmployee.getIdentification());
        employee.setAddress(updatedEmployee.getAddress());
        employee.setWorkplace(updatedEmployee.getWorkplace());
        employee.setBankAccount(updatedEmployee.getBankAccount());
        employee.setBank(updatedEmployee.getBank());

        // Cập nhật Position nếu có
        if (updatedEmployee.getPosition() != null && updatedEmployee.getPosition().getId() != null) {
            Position position = positionRepository.findByName(updatedEmployee.getPosition().getName())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            employee.setPosition(position);
        }

        // Cập nhật Department nếu có
        if (updatedEmployee.getDepartment() != null && updatedEmployee.getDepartment().getId() != null) {
            Department department = departmentRepository.findByName(updatedEmployee.getDepartment().getName())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }

        // Lưu và trả về nhân viên đã cập nhật
        return employeeRepository.save(employee);
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

