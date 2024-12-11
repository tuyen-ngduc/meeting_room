package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.dto.request.Login;
import com.virtual_assistant.meet.dto.request.Register;
import com.virtual_assistant.meet.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Register request) {
        Optional<String> result = accountService.register(request);
        if (result.isPresent()) {
            return ResponseEntity.badRequest().body(result.get());
        } else {
            return ResponseEntity.ok("Đăng ký tài khoản thành công!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login request) {
        Optional<String> result = accountService.login(request);
        if (result.isPresent()) {
            if(result.get().equals("Tên tài khoản không tồn tại") || result.get().equals("Mật khẩu không đúng")) {
               return  ResponseEntity.badRequest().body(result.get());
            }
            else {
                return ResponseEntity.ok().body(result.get());
            }
         }
        else {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống");
        }
    }
}
