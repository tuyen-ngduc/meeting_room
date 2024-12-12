package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.domain.Member;
import com.virtual_assistant.meet.domain.Role;
import com.virtual_assistant.meet.dto.request.CreateMeetingDTO;
import com.virtual_assistant.meet.dto.request.MemberRequestDTO;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import com.virtual_assistant.meet.repository.MeetingRepository;
import com.virtual_assistant.meet.repository.MemberRepository;
import com.virtual_assistant.meet.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public void addEmployeesToMeeting(Long idMeeting, List<MemberRequestDTO> request) {
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc họp có id " + idMeeting));
        for (MemberRequestDTO dto : request) {
            Employee employee = employeeRepository.findById(dto.getIdMember())
                    .orElseThrow(() -> new RuntimeException("Nhân viên có id " + dto.getIdMember() + " không tồn tại"));
            Role role = roleRepository.findByName(dto.getRoleName())
                    .orElseThrow(() -> new RuntimeException("Chức danh " + dto.getRoleName() + " không tồn tại"));

            Member newMember = new Member();
            newMember.setEmployee(employee);
            newMember.setRole(role);
            memberRepository.save(newMember);
            // Làm mới entity meeting để đảm bảo không có vấn đề đồng bộ
            meeting = meetingRepository.findById(idMeeting)
                    .orElseThrow(() -> new RuntimeException("Meeting not found"));

        }
        meetingRepository.save(meeting);

    }
}