package com.virtual_assistant.meet.controller;


import com.virtual_assistant.meet.dto.request.ChangePasswordDTO;
import com.virtual_assistant.meet.dto.request.EmployeeDTO;
import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.dto.response.EmployeeInfoDTO;
import com.virtual_assistant.meet.service.AccountService;
import com.virtual_assistant.meet.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Login request) {
        Optional<String> loginResponse = accountService.login(request);

        if (loginResponse.isPresent() && loginResponse.get().startsWith("Mật khẩu không đúng") || loginResponse.get().startsWith("Tên tài khoản không tồn tại")) {
            return new ResponseEntity<>(loginResponse.get(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(loginResponse.get(), HttpStatus.OK);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<EmployeeInfoDTO> getEmployeeInfo() {
        Optional<EmployeeInfoDTO> employeeInfoOpt = employeeService.getEmployeeInfo();
        return employeeInfoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body(null));
    }



    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody EmployeeDTO employeeDTO) {
        try {
            employeeService.createEmployeeAccount(employeeDTO);
            return ResponseEntity.ok("Thêm nhân viên thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO request) {
        try {
            accountService.changePassword(request.getUsername(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok("Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password/{idEmployee}")
    public ResponseEntity<String> resetPassword(@PathVariable String idEmployee) {
        try {
            accountService.resetPassword(idEmployee);
            return ResponseEntity.ok("Mật khẩu của nhân viên đã được đặt lại về mặc định (ngày sinh)");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
