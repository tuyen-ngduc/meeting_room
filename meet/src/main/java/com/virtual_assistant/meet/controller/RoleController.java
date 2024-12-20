package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Member;
import com.virtual_assistant.meet.domain.Role;
import com.virtual_assistant.meet.service.MemberService;
import com.virtual_assistant.meet.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/roles")

public class RoleController {

    @Autowired
    RoleService roleService;


    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRole());

    }

    // Cập nhật chức danh của thành viên trong cuộc họp
    @PutMapping("/{meetingId}/{memberId}")
    public ResponseEntity<?> updateRoleOfMember(
            @PathVariable long meetingId,
            @PathVariable String memberId,
            @RequestParam String roleName) {

        // Cập nhật chức danh của thành viên
        Member member = roleService.updateRoleOfMember(meetingId, memberId, roleName);

        return ResponseEntity.ok("Sửa thành công");
    }
}
