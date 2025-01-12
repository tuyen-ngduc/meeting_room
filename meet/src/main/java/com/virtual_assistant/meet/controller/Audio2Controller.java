package com.virtual_assistant.meet.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transcript")
public class Audio2Controller {
    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/{filename}")
    public ResponseEntity<String> processAudioFromUploads(@PathVariable("filename") String filename) {
        try {
            // Đường dẫn file trong thư mục "uploads"
            File file = new File(UPLOAD_DIR + filename);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("File not found: " + filename);
            }

            // Tạo HTTP request gửi tới Flask API
            String flaskApiUrl = "http://localhost:5000/api/speech-to-text";

            RestTemplate restTemplate = new RestTemplate();

            // Tạo body request với file đính kèm
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file)); // Gửi file `.wav`

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Gửi POST request tới Flask API
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing audio: " + e.getMessage());
        }
    }
}

