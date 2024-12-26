package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.config.JwtUtil;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.repository.EmployeeRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final EmployeeRepository employeeRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(EmployeeRepository employeeRepository, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<String> login(Login request) {

        Optional<Employee> employeeOpt = employeeRepository.findByUsername(request.getUsername());
        if (employeeOpt.isEmpty()) {
            return Optional.of("Tên tài khoản không tồn tại");
        }
        Employee employee = employeeOpt.get();
        if (passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            String token = jwtUtil.generateToken(employee.getUsername());
            return Optional.of(token);
        } else {
            return Optional.of("Mật khẩu không đúng");
        }
    }



    public void changePassword(String username, String oldPassword, String newPassword) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại!"));

        if (!employee.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác!");
        }
        employee.setPassword(newPassword);
        employeeRepository.save(employee);
    }
}
