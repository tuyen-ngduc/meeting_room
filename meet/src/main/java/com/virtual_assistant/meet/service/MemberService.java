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

import java.sql.SQLOutput;
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
            Employee employee = employeeRepository.findByIdEmployee(dto.getIdMember())
                    .orElseThrow(() -> new RuntimeException("Nhân viên có mã " + dto.getIdMember() + " không tồn tại"));
            List<Member> existingMembers = memberRepository.findMembersByEmployeeAndMeetingTime(
                    employee.getIdEmployee(), meeting.getStartTime());

            // Kiểm tra xem nhân viên này đã được thêm vào cuộc họp hay chưa
            boolean isAlreadyMember = memberRepository.existsByMeetingIdAndEmployeeId(idMeeting, employee.getIdEmployee());

            if (isAlreadyMember) {
                throw new RuntimeException("Nhân viên có mã " + dto.getIdMember() + " đã tham gia cuộc họp này");
            }


//            if (!existingMembers.isEmpty()) {
//                throw new RuntimeException("Nhân viên " + employee.getIdEmployee() + " đã tham gia một cuộc họp cùng giờ.");
//            }


            Role role = roleRepository.findByName(dto.getRoleName())

                    .orElseThrow(() -> new RuntimeException("Chức danh " + dto.getRoleName() + " không tồn tại"));

            // Tạo một Member mới cho cuộc họp này với vai trò tương ứng
            Member newMember = new Member();
            newMember.setEmployee(employee);
            newMember.setRole(role);
            memberRepository.save(newMember); // Lưu Member vào cơ sở dữ liệu

            // Thêm Member vào cuộc họp
            meeting.getMembers().add(newMember);
        }
        meetingRepository.save(meeting);

    }

    public void deleteMember(Long idMeeting, String idMember) {
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new RuntimeException("Cuộc họp không tồn tại"));
        Member member = memberRepository.findByEmployee_IdEmployee(idMember)
                .orElseThrow(() -> new RuntimeException("Thành viên không tồn tại"));
        if(meeting.getMembers().contains(member)) {
            meeting.getMembers().remove(member);
            meetingRepository.save(meeting);
        }
        else {
            throw new RuntimeException("Thành viên không nằm trong cuộc họp này");
        }


    }

        public void updateMemberRole(Long idMeeting, String idMember, String newRole) {
            // Tìm Meeting
            Meeting meeting = meetingRepository.findById(idMeeting)
                    .orElseThrow(() -> new RuntimeException("Meeting not found with id " + idMeeting));

            // Tìm Member
            Member member = memberRepository.findByEmployee_IdEmployee(idMember)
                    .orElseThrow(() -> new RuntimeException("Member not found with id " + idMember));

            // Kiểm tra xem Member có nằm trong Meeting không
            if (!meeting.getMembers().contains(member)) {
                throw new RuntimeException("Member does not belong to the specified meeting");
            }

            // Tìm Role mới từ database
            Role role = roleRepository.findByName(newRole)
                    .orElseThrow(() -> new RuntimeException("Role not found with name " + newRole));

            // Cập nhật Role cho Member
            member.setRole(role);

            // Lưu lại
            memberRepository.save(member);
        }
    }



