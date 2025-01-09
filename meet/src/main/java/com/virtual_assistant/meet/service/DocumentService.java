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


    public void saveDocuments(MultipartFile[] files, long meetingId) throws IOException {
        // Lấy thông tin cuộc họp từ cơ sở dữ liệu
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        // Lấy thuộc tính rememberCode của cuộc họp
        String rememberCode = meeting.getRememberCode();
        if (rememberCode == null || rememberCode.isEmpty()) {
            throw new RuntimeException("Meeting rememberCode is missing.");
        }

        // Đường dẫn thư mục con (documents/{rememberCode})
        String uploadDir = "documents/" + rememberCode + "/";
        Path uploadPath = Paths.get(uploadDir);

        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Đường dẫn file trong thư mục con
                Path filePath = uploadPath.resolve(file.getOriginalFilename());

                // Lưu file vào thư mục con
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Tạo đối tượng Document và lưu vào cơ sở dữ liệu
                Document document = new Document();
                document.setName(file.getOriginalFilename());
                document.setPath(filePath.toString());
                document.setMeeting(meeting);

                // Lưu thông tin file vào cơ sở dữ liệu
                documentRepository.save(document);
            }
        }
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


