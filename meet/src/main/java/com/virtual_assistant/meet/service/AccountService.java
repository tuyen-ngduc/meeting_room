package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.config.JwtUtil;
import com.virtual_assistant.meet.domain.Account;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.dto.request.Register;
import com.virtual_assistant.meet.dto.response.EmployeeInfoDTO;
import com.virtual_assistant.meet.repository.AccountRepository;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    public Optional<String> register(Register request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(request.getUsername());
        if (accountOpt.isPresent()) {
            return Optional.of("Tên tài khoản đã tồn tại");
        }
        Optional<Employee> employeeOpt = employeeRepository.findById(request.getIdEmployee());
        if (!employeeOpt.isPresent()) {
            return Optional.of("Nhân viên không tồn tại");
        }

        Employee employee = employeeOpt.get();
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setEmployee(employee);
        try {
            accountRepository.save(account);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of("Lỗi khi tạo tài khoản");
        }
    }

    public Optional<String> login(Login request) {
        Optional<Account> accountOpt = accountRepository.findByUsername(request.getUsername());
        if (!accountOpt.isPresent()) {
            return Optional.of("Tên tài khoản không tồn tại");
        } else {
            Account account = accountOpt.get();
            if (account.getPassword().equals(request.getPassword())) {
                String token = jwtUtil.generateToken(account.getUsername());
                return Optional.of(token);
            } else {
                return Optional.of("Mật khẩu không đúng");
            }
        }
    }



    public Optional<EmployeeInfoDTO> getEmployeeInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Account> account = accountRepository.findByUsername(username);

        return account.map(acc -> {
            Employee employee = acc.getEmployee();

            EmployeeInfoDTO info = new EmployeeInfoDTO();
            info.setName(employee.getName());
            info.setDob(employee.getDob());
            info.setPhoneNumber(employee.getPhoneNumber());
            info.setAddress(employee.getAddress());
            info.setDegree(employee.getDegree());
            info.setWorkplace(employee.getWorkplace());
            info.setDepartment(employee.getDepartment().getName());
            info.setPosition(employee.getPosition().getName());

            return info;
        });
    }


}

