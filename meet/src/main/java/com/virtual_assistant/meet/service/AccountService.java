package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.config.JwtUtil;
import com.virtual_assistant.meet.domain.Account;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.dto.request.Register;
import com.virtual_assistant.meet.dto.response.AccountDTO;
import com.virtual_assistant.meet.dto.response.EmployeeDTO;
import com.virtual_assistant.meet.dto.response.EmployeeInfoDTO;
import com.virtual_assistant.meet.repository.AccountRepository;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EmployeeRepository employeeRepository;



    public boolean changePasswordForUser(String username, String newPassword) {
        // Kiểm tra tài khoản người dùng từ username
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với tên đăng nhập: " + username));

        // Cập nhật mật khẩu mới
        account.setPassword(newPassword);
        accountRepository.save(account);
        return true;
    }

    // Xóa tài khoản
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        accountRepository.delete(account);
    }

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

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getUsername(),
                        account.getPassword(),
                        account.getEmployee() != null
                                ? new EmployeeDTO(
                                account.getEmployee().getId(),
                                account.getEmployee().getName()
                        )
                                : null
                ))
                .collect(Collectors.toList());
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
            info.setId(employee.getId());
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

