package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.dto.request.CreateMeetingDTO;
import com.virtual_assistant.meet.dto.request.MeetingStatusUpdateRequest;
import com.virtual_assistant.meet.dto.request.MemberRequestDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import com.virtual_assistant.meet.enums.Status;
import com.virtual_assistant.meet.service.MeetingService;
import com.virtual_assistant.meet.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        try {
            memberService.addEmployeesToMeeting(idMeeting, request);
            return ResponseEntity.ok("Thêm nhân viên vào cuộc họp thành công!");
        } catch (RuntimeException e) {
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
            Meeting meeting = meetingService.createMeeting(request);


            return ResponseEntity.ok(meeting.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/my-meetings")
    public ResponseEntity<List<MeetingDTO>> getMeetingsByUser() {
        List<MeetingDTO> meetings = meetingService.getMeetingsByUser();
        return ResponseEntity.ok(meetings);
    }


    @GetMapping("/meeting-id/{idMeeting}")
    public ResponseEntity<MeetingDTO> getMeetingById(@PathVariable long idMeeting) {
        MeetingDTO meetingDTO = meetingService.getMeetingById(idMeeting);
        return ResponseEntity.ok(meetingDTO);
    }

    @PutMapping("/{idMeeting}")
    public ResponseEntity<String> updateMeeting(
            @PathVariable Long idMeeting,
            @RequestBody com.virtual_assistant.meet.dto.request.MeetingDTO meetingDTO) {
        try {
            meetingService.updateMeeting(idMeeting, meetingDTO);
            return ResponseEntity.ok("Meeting updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update-status")
    public ResponseEntity<String> updateMeetingStatus(@RequestBody MeetingStatusUpdateRequest request) {
        try {
            meetingService.updateMeetingStatus(request.getIdMeeting(), request.getStatus());
            return ResponseEntity.ok("Trạng thái cuộc họp đã được cập nhật thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cập nhật trạng thái thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/{idMeeting}/complete")
    public ResponseEntity<?> completeMeeting(@PathVariable Long idMeeting) {
        try {
            meetingService.completeMeeting(idMeeting);
            return ResponseEntity.ok("Hoàn thành cuộc họp");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}