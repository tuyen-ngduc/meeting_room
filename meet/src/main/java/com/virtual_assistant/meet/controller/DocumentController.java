package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.dto.response.DocumentDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
