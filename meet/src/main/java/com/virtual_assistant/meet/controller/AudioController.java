package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/audio")
public class AudioController {
    @Autowired
    private MeetingRepository meetingRepository;
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/uploads/{rememberCode}")
    public ResponseEntity<String> uploadAudio(
            @PathVariable("rememberCode") String rememberCode,
            @RequestParam("audio") MultipartFile audioFile) {
        try {
            // Kiểm tra xem rememberCode có tồn tại trong bảng meeting không
            Optional<Meeting> meetingOptional = meetingRepository.findByRememberCode(rememberCode);
            if (meetingOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Meeting with rememberCode " + rememberCode + " does not exist.");
            }

            // Tạo đường dẫn thư mục theo mã cuộc họp (rememberCode)
            Path directoryPath = Paths.get("uploads/" + rememberCode);
            Files.createDirectories(directoryPath);

            // Đường dẫn lưu file audio trong thư mục mã cuộc họp
            Path filePath = directoryPath.resolve(audioFile.getOriginalFilename());

            // Lưu file vào thư mục
            Files.write(filePath, audioFile.getBytes());

            return ResponseEntity.ok("Audio uploaded successfully to folder: " + rememberCode);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading audio: " + e.getMessage());
        }
    }

    @GetMapping("/transcript/{rememberCode}")
    public ResponseEntity<String> processAudioFromUploads(@PathVariable("rememberCode") String rememberCode) {
        try {
            Optional<Meeting> meetingOptional = meetingRepository.findByRememberCode(rememberCode);
            if (meetingOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Meeting with rememberCode " + rememberCode + " does not exist.");
            }

            // Đường dẫn thư mục "uploads/{rememberCode}"
            File uploadDir = new File(UPLOAD_DIR + rememberCode);
            if (!uploadDir.exists() || !uploadDir.isDirectory()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Directory not found: " + uploadDir.getPath());
            }

            // Lấy tất cả các file trong thư mục
            File[] files = uploadDir.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".flac")); // Có thể thay đổi tùy theo định dạng file âm thanh

            if (files == null || files.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No audio files found in the directory.");
            }

            // Tạo HTTP request gửi tới Flask API
            String flaskApiUrl = "http://localhost:5000/api/speech-to-text";
            RestTemplate restTemplate = new RestTemplate();

            // Tạo body request với nhiều file đính kèm
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            for (File file : files) {
                body.add("file", new FileSystemResource(file)); // Gửi từng file âm thanh
            }
            body.add("rememberCode", rememberCode); // Thêm rememberCode vào form data

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Gửi POST request tới Flask API
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok("Transcripts saved successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error processing audio: " + response.getBody());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing audio: " + e.getMessage());
        }
    }





}

