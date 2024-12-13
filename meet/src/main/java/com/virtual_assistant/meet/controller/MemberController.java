package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.service.MeetingService;
import com.virtual_assistant.meet.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    MemberService memberService;

    @DeleteMapping("/{idMeeting}/{idMember}")
    public ResponseEntity<String> deleteMember(@PathVariable long idMeeting, @PathVariable String idMember){
                try {
                    memberService.deleteMember(idMeeting, idMember);
                    return ResponseEntity.ok("Xóa thành công");
                }catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }

    }

    @PutMapping("/{idMeeting}/{idMember}/role")
    public ResponseEntity<String> updateMemberRole(
            @PathVariable Long idMeeting,
            @PathVariable String idMember,
            @RequestParam String newRole) {
        try {
            memberService.updateMemberRole(idMeeting, idMember, newRole);
            return ResponseEntity.ok("Role updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
