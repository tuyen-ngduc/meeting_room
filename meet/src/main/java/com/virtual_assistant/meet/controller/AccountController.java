package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Account;
import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.dto.request.PasswordChangeDTO;
import com.virtual_assistant.meet.dto.request.Register;
import com.virtual_assistant.meet.dto.response.AccountDTO;
import com.virtual_assistant.meet.dto.response.EmployeeInfoDTO;
import com.virtual_assistant.meet.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }


    @PutMapping("/admin/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        try {
            boolean result = accountService.changePasswordForUser(passwordChangeDTO.getUsername(), passwordChangeDTO.getNewPassword());
            if (result) {
                return ResponseEntity.ok("Đổi mật khẩu thành công");
            } else {
                return ResponseEntity.status(400).body("Lỗi khi đổi mật khẩu");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // API Xóa tài khoản
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Register request) {
        Optional<String> result = accountService.register(request);
        if (result.isPresent()) {
            return ResponseEntity.badRequest().body(result.get());
        } else {
            return ResponseEntity.ok("Đăng ký tài khoản thành công!");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<EmployeeInfoDTO> getEmployeeInfo() {
        Optional<EmployeeInfoDTO> info = accountService.getEmployeeInfo();
        return info.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login request) {
        Optional<String> result = accountService.login(request);
        if (result.isPresent()) {
            if (result.get().equals("Tên tài khoản không tồn tại") || result.get().equals("Mật khẩu không đúng")) {
                return ResponseEntity.badRequest().body(result.get());
            } else {
                return ResponseEntity.ok().body(result.get());
            }
        } else {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống");
        }
    }
}
