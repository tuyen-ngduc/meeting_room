package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Document;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.dto.response.DocumentDTO;
import com.virtual_assistant.meet.dto.response.DocumentFileDTO;
import com.virtual_assistant.meet.repository.DocumentRepository;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import com.virtual_assistant.meet.repository.MeetingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class DocumentService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    DocumentRepository documentRepository;


    public List<DocumentDTO> getDocumentsByUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tìm tài khoản từ username và lấy Employee tương ứng
        Optional<Employee> currentEmployee = employeeRepository.findByUsername(username);
        boolean isSecretary = currentEmployee.get().getPosition().getName().equals("Thư ký");

        List<Meeting> meetings;

        if (isSecretary) {
            meetings = meetingRepository.findAll();
        } else {
            meetings = meetingRepository.findByMembersEmployee(currentEmployee.get());
        }

        return meetings.stream().map(meeting -> {
            DocumentDTO dto = new DocumentDTO();
            dto.setName(meeting.getName());
            dto.setRememberCode(meeting.getRememberCode());
            dto.setDepartment(meeting.getDepartment().getName());
            dto.setTranscript(meeting.getTranscript());
            return dto;
        }).collect(Collectors.toList());
    }


    // Lưu file và cập nhật thông tin vào cơ sở dữ liệu
    public void saveDocument(MultipartFile file, long meetingId) throws IOException {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
        String uploadDir = "documents/";
        Path path = Paths.get(uploadDir + file.getOriginalFilename());

        // Lưu file vào thư mục
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // Tạo đối tượng Document và lưu vào cơ sở dữ liệu
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setPath(path.toString());
        document.setMeeting(meeting);

        documentRepository.save(document);
    }

    public List<DocumentFileDTO> getAllDocuments() {

            List<Document> documents = documentRepository.findAll();
            List<DocumentFileDTO> documentDTOs = new ArrayList<>();

            for (Document document : documents) {
                DocumentFileDTO dto = new DocumentFileDTO();
                dto.setId(document.getId());
                dto.setName(document.getName());
                dto.setPath(document.getPath());
                dto.setIdMeeting(document.getMeeting().getId());

                documentDTOs.add(dto);
            }
            return documentDTOs;
        }

    public List<DocumentFileDTO> getDocumentsByMeetingId(long meetingId) {
        List<Document> documents = documentRepository.findByMeetingId(meetingId);

        // Chuyển đổi List<Document> thành List<DocumentFileDTO>
        List<DocumentFileDTO> documentFileDTOs = new ArrayList<>();
        for (Document document : documents) {
            DocumentFileDTO dto = new DocumentFileDTO();
            dto.setId(document.getId());
            dto.setName(document.getName());
            dto.setPath(document.getPath());
            dto.setIdMeeting(document.getMeeting().getId()); // Chỉ lấy idMeeting
            documentFileDTOs.add(dto);
        }

        return documentFileDTOs;
    }
}


