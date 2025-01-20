package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Department;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Position;
import com.virtual_assistant.meet.dto.request.EmployeeDTO;


import com.virtual_assistant.meet.dto.request.UpdateProfileRequest;
import com.virtual_assistant.meet.dto.response.EmployeeInfoDTO;
import com.virtual_assistant.meet.repository.DepartmentRepository;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import com.virtual_assistant.meet.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           PositionRepository positionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<com.virtual_assistant.meet.dto.response.EmployeeDTO> getAllEmployee() {
        return employeeRepository.findEmployee();
    }

    public void createEmployeeAccount(EmployeeDTO employeeDTO) {
        if (employeeRepository.findByIdEmployee(employeeDTO.getIdEmployee()).isPresent()) {
            throw new IllegalArgumentException("Nhân viên này đã có tài khoản!");
        }
        if (employeeRepository.findByUsername(employeeDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
        }
        Department department = departmentRepository.findByName(employeeDTO.getDepartment())
                .orElseThrow(() -> new IllegalArgumentException("Phòng ban không tồn tại"));

        Position position = positionRepository.findByName(employeeDTO.getPosition())
                .orElseThrow(() -> new IllegalArgumentException("Chức vụ không tồn tại"));

        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setIdEmployee(employeeDTO.getIdEmployee());
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setDegree(employeeDTO.getDegree());
        employee.setDob(employeeDTO.getDob());
        employee.setUsername(employeeDTO.getIdEmployee());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String rawPassword = employeeDTO.getDob().format(formatter);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        employee.setPassword(encodedPassword);
        employee.setBankAccount(employeeDTO.getBankAccount());
        employee.setBank(employeeDTO.getBank());
        employee.setAddress(employeeDTO.getAddress());
        employee.setIdentification((employeeDTO.getIdentification()));
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setWorkplace(employeeDTO.getWorkplace());
        employeeRepository.save(employee);
    }

    // Thêm nhân viên mới
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        // Kiểm tra xem nhân viên đã tồn tại chưa (dựa vào id nhân viên kiểu String)
        if (employeeRepository.existsById(employeeDTO.getIdEmployee())) {
            throw new RuntimeException("Nhân viên có id " + employeeDTO.getIdEmployee() + " đã tồn tại");
        }

        Employee newEmployee = new Employee();
        newEmployee.setIdEmployee(employeeDTO.getIdEmployee());
        newEmployee.setName(employeeDTO.getName());
        newEmployee.setBank(employeeDTO.getBank());
        newEmployee.setAddress(employeeDTO.getAddress());
        newEmployee.setPosition(positionRepository.findByName(employeeDTO.getPosition()).orElseThrow(() -> new RuntimeException("Position not found")));
        newEmployee.setDepartment(departmentRepository.findByName(employeeDTO.getDepartment()).orElseThrow(() -> new RuntimeException("Department not found")));
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
        // Kiểm tra xem nhân viên có tồn tại trong cơ sở dữ liệu không
        Employee existingEmployee = employeeRepository.findByIdEmployee(employeeDTO.getIdEmployee())
                .orElseThrow(() -> new RuntimeException("Nhân viên có id " + employeeDTO.getIdEmployee() + " không tồn tại"));

        // Cập nhật các thông tin mới từ DTO vào thực thể đã tồn tại
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setBank(employeeDTO.getBank());
        existingEmployee.setAddress(employeeDTO.getAddress());
        existingEmployee.setPosition(
                positionRepository.findByName(employeeDTO.getPosition())
                        .orElseThrow(() -> new RuntimeException("Position not found"))
        );
        existingEmployee.setDepartment(
                departmentRepository.findByName(employeeDTO.getDepartment())
                        .orElseThrow(() -> new RuntimeException("Department not found"))
        );
        existingEmployee.setDob(employeeDTO.getDob());
        existingEmployee.setDegree(employeeDTO.getDegree());
        existingEmployee.setIdentification(employeeDTO.getIdentification());
        existingEmployee.setWorkplace(employeeDTO.getWorkplace());
        existingEmployee.setPhoneNumber(employeeDTO.getPhoneNumber());
        existingEmployee.setBankAccount(employeeDTO.getBankAccount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String rawPassword = employeeDTO.getDob().format(formatter);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        existingEmployee.setPassword(encodedPassword);

        // Lưu thay đổi vào cơ sở dữ liệu
        return employeeRepository.save(existingEmployee);
    }



    // Xóa nhân viên
    public void deleteEmployee(String id) {
        Employee employee = employeeRepository.findByIdEmployee(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);
    }

    // Tìm nhân viên theo id
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Optional<EmployeeInfoDTO> getEmployeeInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));
        EmployeeInfoDTO info = new EmployeeInfoDTO();
        info.setId(employee.getIdEmployee());
        info.setName(employee.getName());
        info.setDob(employee.getDob());
        info.setPhoneNumber(employee.getPhoneNumber());
        info.setAddress(employee.getAddress());
        info.setDegree(employee.getDegree());
        info.setDepartment(employee.getDepartment().getName());
        info.setPosition(employee.getPosition().getName());
        info.setEmail(employee.getEmail());
        info.setBank(employee.getBank());
        info.setBankAccount(employee.getBankAccount());
        info.setWorkplace(employee.getWorkplace());

        return Optional.of(info);
    }

    public void updateEmployeeProfile(UpdateProfileRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = employeeRepository.findByUsername(currentUser)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
            employee.setPhoneNumber(request.getPhoneNumber());
            employee.setAddress(request.getAddress());
            employee.setEmail(request.getEmail());
            employee.setWorkplace(request.getWorkplace());
            employee.setBank(request.getBank());
            employee.setBankAccount(request.getBankAccount());
            employee.setIdentification(request.getIdentification());
        employeeRepository.save(employee);
    }
}



