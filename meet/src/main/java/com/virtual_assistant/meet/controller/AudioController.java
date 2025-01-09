package com.virtual_assistant.meet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload-audio")
public class AudioController {

    @PostMapping
    public ResponseEntity<String> uploadAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {
            // Lưu file vào thư mục tạm
            Path path = Paths.get("uploads/" + audioFile.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, audioFile.getBytes());

            return ResponseEntity.ok("Audio uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading audio");
        }
    }
}

