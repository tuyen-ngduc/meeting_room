package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Document;
import com.virtual_assistant.meet.dto.response.DocumentDTO;
import com.virtual_assistant.meet.dto.response.DocumentFileDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @GetMapping("/my-document")
    public ResponseEntity<List<DocumentDTO>> getAllMeetings() {
        List<DocumentDTO> meetings = documentService.getDocumentsByUser();
        return ResponseEntity.ok(meetings);
    }

    @PostMapping("/upload/{meetingId}")
    public ResponseEntity<String> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable("meetingId") long meetingId) {
        try {
            if (files == null || files.length == 0) {
                return ResponseEntity.badRequest().body("No files were uploaded.");
            }

            // Gọi service để lưu tất cả các file
            documentService.saveDocuments(files, meetingId);

            return ResponseEntity.ok("Files uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading files: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<DocumentFileDTO>> getAllDocuments() {
        List<DocumentFileDTO> documents = documentService.getAllDocuments();
        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(documents);
        }
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{idMeeting}")
    public ResponseEntity<List<DocumentFileDTO>> getDocumentsByMeetingId(@PathVariable long idMeeting) {
        List<DocumentFileDTO> documents = documentService.getDocumentsByMeetingId(idMeeting);
        if (documents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(documents);
        }
        return ResponseEntity.ok(documents);
    }



}
