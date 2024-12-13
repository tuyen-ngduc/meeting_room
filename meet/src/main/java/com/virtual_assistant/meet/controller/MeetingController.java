package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.dto.request.CreateMeetingDTO;
import com.virtual_assistant.meet.dto.request.MemberRequestDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import com.virtual_assistant.meet.service.MeetingService;
import com.virtual_assistant.meet.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/{idMeeting}")
    public ResponseEntity<List<MemberDTO>> getMembersByMeetingId(@PathVariable Long idMeeting) {
        List<MemberDTO> members = meetingService.getMembersByMeetingId(idMeeting);
        return ResponseEntity.ok(members);
    }
    @PostMapping("/{idMeeting}/add")
    public ResponseEntity<String> addEmployeesToMeeting(@PathVariable Long idMeeting, @RequestBody List<MemberRequestDTO> request) {
        try{
            memberService.addEmployeesToMeeting(idMeeting, request);
            return ResponseEntity.ok("Thêm nhân viên vào cuộc họp thành công!");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @PutMapping("/{idMeeting}/{pathRoom}")
    public ResponseEntity<Void> addPathRoom(@PathVariable long idMeeting, @PathVariable String pathRoom) {
        try {
            meetingService.addPathRoom(idMeeting, pathRoom);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/create")
    public ResponseEntity<?> createMeeting(@RequestBody CreateMeetingDTO request) {
        try {
            // Gọi service để tạo cuộc họp
            Meeting meeting = meetingService.createMeeting(request);
            return ResponseEntity.status(HttpStatus.OK).body("Tạo cuộc họp thành công");
        } catch (RuntimeException e) {
            // Xử lý lỗi khi tạo file transcript hoặc lỗi khác
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/my-meetings")
    public ResponseEntity<List<MeetingDTO>> getAllMeetings() {
        List<MeetingDTO> meetings = meetingService.getMeetingsByUser();
        return ResponseEntity.ok(meetings);
    }




}
