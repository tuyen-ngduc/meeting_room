package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.config.JwtUtil;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.repository.EmployeeRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Transactional
    public void resetPassword(String idEmployee) {
        // Kiểm tra xem nhân viên có tồn tại hay không
        Employee employee = employeeRepository.findByIdEmployee(idEmployee)
                .orElseThrow(() -> new IllegalArgumentException("Nhân viên không tồn tại với mã: " + idEmployee));

        // Lấy ngày sinh của nhân viên
        LocalDate dob = employee.getDob();
        if (dob == null) {
            throw new IllegalArgumentException("Nhân viên chưa có thông tin ngày sinh!");
        }

        // Format ngày sinh theo định dạng mật khẩu
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String rawPassword = dob.format(formatter);

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Cập nhật lại mật khẩu
        employee.setPassword(encodedPassword);

        // Lưu thay đổi vào cơ sở dữ liệu
        employeeRepository.save(employee);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại!"));

        if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác!");
        }
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        employee.setPassword(encodedNewPassword);
        employeeRepository.save(employee);
    }
}
