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

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Lấy Role theo ID
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    // Thêm Role mới
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // Cập nhật Role
    public Role updateRole(Long id, Role updatedRole) {
        return roleRepository.findById(id).map(role -> {
            role.setName(updatedRole.getName());
            role.setDescription(updatedRole.getDescription());
            return roleRepository.save(role);
        }).orElseThrow(() -> new RuntimeException("Role không tồn tại với id: " + id));
    }

    // Xóa Role
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role không tồn tại với id: " + id);
        }
        roleRepository.deleteById(id);
    }

}
