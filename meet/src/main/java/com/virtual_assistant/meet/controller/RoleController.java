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


    // Lấy danh sách tất cả Roles
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // Lấy thông tin Role theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Thêm Role mới
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    // Cập nhật Role
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role updatedRole) {
        try {
            return ResponseEntity.ok(roleService.updateRole(id, updatedRole));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa Role
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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
