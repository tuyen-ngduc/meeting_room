package com.virtual_assistant.meet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/process-audio")
public class TranscriptionController {

    @PostMapping
    public ResponseEntity<String> processAudio(@RequestParam("audio") MultipartFile audioFile) {
        try {
            // Lưu file tạm thời
            Path tempFile = Files.createTempFile("audio", ".wav");
            Files.write(tempFile, audioFile.getBytes());

            // Gửi file tới Flask API
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/transcribe"))
                    .POST(HttpRequest.BodyPublishers.ofFile(tempFile))
                    .header("Content-Type", "multipart/form-data")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Files.delete(tempFile); // Xóa file tạm

            return ResponseEntity.ok(response.body());
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing audio");
        }
    }
}

