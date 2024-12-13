package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.*;
import com.virtual_assistant.meet.dto.request.CreateMeetingDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.dto.response.MemberByIdDTO;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import com.virtual_assistant.meet.enums.Status;
import com.virtual_assistant.meet.repository.AccountRepository;
import com.virtual_assistant.meet.repository.DepartmentRepository;
import com.virtual_assistant.meet.repository.MeetingRepository;
import com.virtual_assistant.meet.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    AccountRepository accountRepository;


    public MeetingDTO getMeetingById(long idMeeting) {
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id " + idMeeting));

        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(meeting.getId());
        meetingDTO.setName(meeting.getName());
        meetingDTO.setStartTime(meeting.getStartTime());
        meetingDTO.setExpectedEndTime(meeting.getExpectedEndTime());
        meetingDTO.setDepartment(meeting.getDepartment().getName());
        meetingDTO.setRoom(meeting.getRoom().getName());
        meetingDTO.setStatus(meeting.getStatus().getDescription());
        meetingDTO.setRememberCode(meeting.getRememberCode());


        List<MemberByIdDTO> memberDTOs = meeting.getMembers().stream()
                .map(member -> {
                    MemberByIdDTO memberDTO = new MemberByIdDTO();
                    Employee employee = member.getEmployee();
                    Role role = member.getRole();
                    memberDTO.setIdMember(employee.getId());
                    memberDTO.setName(employee.getName());
                    memberDTO.setRole(role.getName());

                    return memberDTO;
                })
                .collect(Collectors.toList());

        meetingDTO.setMembers(memberDTOs);

        return meetingDTO;
    }


    public List<MemberDTO> getMembersByMeetingId(Long idMeeting) {
        return meetingRepository.findMembersByMeetingId(idMeeting);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Meeting createMeeting(CreateMeetingDTO request) {
        // Kiểm tra phòng ban
        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new RuntimeException("Phòng ban không tồn tại: " + request.getDepartment()));

        // Kiểm tra phòng họp
        Room room = roomRepository.findByName(request.getRoom())
                .orElseThrow(() -> new RuntimeException("Phòng họp không tồn tại: " + request.getRoom()));

        // Thời gian bắt đầu và kết thúc dự kiến
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime expectedEndTime = request.getExpectedEndTime();
        LocalDateTime endTime = null;

        if (expectedEndTime.isBefore(startTime)) {
            throw new RuntimeException("Thời gian kết thúc dự kiến không thể trước thời gian bắt đầu.");
        }

        if (!room.getName().equalsIgnoreCase("Online")) {
            List<Meeting> conflictingMeetings = meetingRepository.findConflictingMeetings(room.getId(), startTime, expectedEndTime);
            if (!conflictingMeetings.isEmpty()) {
                throw new RuntimeException("Phòng họp " + room.getName() + " đã có cuộc họp khác trong khung giờ này.");
            }

        }


        // Tạo cuộc họp mới
        Meeting meeting = new Meeting();
        meeting.setName(request.getName());
        meeting.setRememberCode(request.getRememberCode());
        meeting.setStartTime(startTime);
        meeting.setExpectedEndTime(expectedEndTime);
        meeting.setEndTime(endTime);
        meeting.setRoom(room);
        meeting.setDepartment(department);
        meeting.setStatus(getMeetingStatus(startTime, endTime));


        try {
            String transcriptFilePath = saveMeetingTranscript(meeting);
            meeting.setTranscript(transcriptFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file transcript: " + e.getMessage(), e);
        }
        return meetingRepository.save(meeting);


    }

    public Status getMeetingStatus(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (nowTime.isBefore(startTime)) {
            // Tính khoảng cách thời gian giữa thời gian hiện tại và thời gian bắt đầu
            long hoursDifference = Duration.between(nowTime, startTime).toHours();
            if (hoursDifference >= 36) {
                return Status.NOT_STARTED;
            } else {
                return Status.UPCOMING;
            }
        } else {
            if (endTime != null) {
                return Status.COMPLETED;
            } else {
                return Status.ONGOING;
            }
        }
    }


    private String saveMeetingTranscript(Meeting meeting) throws IOException {
        String meetingFolderName = meeting.getRememberCode();
        Path meetingFolderPath = Paths.get("meeting_transcripts", meetingFolderName);
        File meetingFolder = new File(meetingFolderPath.toString());
        if (!meetingFolder.exists()) {
            meetingFolder.mkdirs();
        }

        String transcriptFileName = "transcript.txt";
        Path transcriptFilePath = meetingFolderPath.resolve(transcriptFileName);

        try (FileWriter fileWriter = new FileWriter(transcriptFilePath.toFile())) {
            fileWriter.write("Transcript for meeting: " + meeting.getName());
        }

        return transcriptFilePath.toString();
    }


    public List<MeetingDTO> getMeetingsByUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tìm tài khoản từ username và lấy Employee tương ứng
        Optional<Account> account = accountRepository.findByUsername(username);
        Employee currentEmployee = account.get().getEmployee();
        // Kiểm tra vị trí của nhân viên, nếu là "Thư ký", lấy tất cả cuộc họp
        boolean isSecretary = currentEmployee.getPosition().getName().equals("Thư ký");

        List<Meeting> meetings;

        if (isSecretary) {
            // Nếu là "Thư ký", lấy tất cả cuộc họp
            meetings = meetingRepository.findAll();
        } else {
            // Nếu không, lấy danh sách cuộc họp mà Employee tham gia qua bảng meeting_member
            meetings = meetingRepository.findByMembersEmployee(currentEmployee);
        }

        return meetings.stream().map(meeting -> {
            MeetingDTO dto = new MeetingDTO();
            dto.setId(meeting.getId());
            dto.setName(meeting.getName());
            dto.setStartTime(meeting.getStartTime());
            dto.setExpectedEndTime(meeting.getExpectedEndTime());
            dto.setDepartment(meeting.getDepartment().getName());
            dto.setRoom(meeting.getRoom().getName());  // Lấy tên phòng họp
            dto.setStatus(meeting.getStatus().getDescription());
            dto.setRememberCode(meeting.getRememberCode());
            if (meeting.getDocuments() != null && !meeting.getDocuments().isEmpty()) {

                Document document = meeting.getDocuments().get(0);
                dto.setPath(document.getPath());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public void addPathRoom(long id, String path) {
        Optional<Meeting> meetingOptional = meetingRepository.findById(id);

        if (meetingOptional.isPresent()) {
            Meeting meeting = meetingOptional.get();
            meeting.setPath(path);
            meetingRepository.save(meeting);
        } else {
            throw new RuntimeException("Meeting not found with id: " + id);
        }

    }
}
