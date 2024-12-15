package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.domain.Member;
import com.virtual_assistant.meet.domain.Role;
import com.virtual_assistant.meet.repository.MeetingRepository;
import com.virtual_assistant.meet.repository.MemberRepository;
import com.virtual_assistant.meet.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MemberRepository memberRepository;


    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    // Cập nhật chức danh của thành viên trong cuộc họp
    public Member updateRoleOfMember(Long meetingId, String memberId, String newRoleId) {
        // Kiểm tra xem cuộc họp có tồn tại không
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new RuntimeException("Meeting not found"));

        // Kiểm tra xem Member có tồn tại không
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        // Kiểm tra xem chức danh mới có tồn tại không
        Role newRole = roleRepository.findByName(newRoleId).orElseThrow(() -> new RuntimeException("New Role not found"));

        // Cập nhật chức danh của Member
        member.setRole(newRole);

        // Lưu Member với chức danh mới
        return memberRepository.save(member);
    }

}
