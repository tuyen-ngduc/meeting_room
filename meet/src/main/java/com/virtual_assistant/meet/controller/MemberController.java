package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Role;
import com.virtual_assistant.meet.service.MeetingService;
import com.virtual_assistant.meet.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MemberController(MemberService memberService, SimpMessagingTemplate messagingTemplate) {
        this.memberService = memberService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/{employeeId}/{meetingId}")
    public ResponseEntity<String> getRoleByMemberAndMeeting(
            @PathVariable String employeeId,
            @PathVariable Long meetingId) {
        try {
            String roleName = memberService.getRoleNameByMemberAndMeeting(employeeId, meetingId);
            return ResponseEntity.ok(roleName);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/{idMeeting}/{idMember}")
    public ResponseEntity<String> deleteMember(@PathVariable long idMeeting, @PathVariable String idMember){
                try {
                    memberService.deleteMember(idMeeting, idMember);
                    // Gửi thông báo qua WebSocket tới topic "/topic/members"
                    String message = "Thành viên " + idMember + " đã bị xóa khỏi cuộc họp " + idMeeting;
                    messagingTemplate.convertAndSend("/topic/members", message);
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
