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
            List<Member> existingMembers = memberRepository.findMembersByEmployeeAndMeetingTime(
                    employee.getId(), meeting.getStartTime());

            if (!existingMembers.isEmpty()) {
                throw new RuntimeException("Nhân viên " + employee.getId() + " đã tham gia cuộc họp khác cùng giờ.");
            }

            Role role = roleRepository.findByName(dto.getRoleName())

                    .orElseThrow(() -> new RuntimeException("Chức danh " + dto.getRoleName() + " không tồn tại"));
            Member existingMember = memberRepository.findByEmployeeId(employee.getId());

            if (existingMember == null) {
                existingMember = new Member();
                existingMember.setEmployee(employee);
                existingMember.setRole(role);
                memberRepository.save(existingMember);
            }

            // Thêm Member vào danh sách members của cuộc họp
            meeting.getMembers().add(existingMember);
        }


        meetingRepository.save(meeting);

    }
}