package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Account;
import com.virtual_assistant.meet.domain.Document;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.dto.response.DocumentDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.repository.AccountRepository;
import com.virtual_assistant.meet.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class DocumentService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    MeetingRepository meetingRepository;


    public List<DocumentDTO> getDocumentsByUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tìm tài khoản từ username và lấy Employee tương ứng
        Optional<Account> account = accountRepository.findByUsername(username);
        Employee currentEmployee = account.get().getEmployee();
        // Kiểm tra vị trí của nhân viên, nếu là "Thư ký", lấy tất cả cuộc họp
        boolean isSecretary = currentEmployee.getPosition().getName().equals("Thư ký");

        List<Meeting> meetings;

        if (isSecretary) {
            meetings = meetingRepository.findAll();
        } else {
            meetings = meetingRepository.findByMembersEmployee(currentEmployee);
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

}
