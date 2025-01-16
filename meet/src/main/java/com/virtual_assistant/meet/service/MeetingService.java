package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.*;
import com.virtual_assistant.meet.dto.request.CreateMeetingDTO;
import com.virtual_assistant.meet.dto.response.MeetingDTO;
import com.virtual_assistant.meet.dto.response.MemberByIdDTO;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import com.virtual_assistant.meet.enums.Status;
import com.virtual_assistant.meet.repository.DepartmentRepository;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import com.virtual_assistant.meet.repository.MeetingRepository;
import com.virtual_assistant.meet.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    EmployeeRepository employeeRepository;


    public MeetingDTO getMeetingById(long idMeeting) {
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id " + idMeeting));

        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setId(meeting.getId());
        meetingDTO.setName(meeting.getName());
        meetingDTO.setStartTime(meeting.getStartTime());
        meetingDTO.setDepartment(meeting.getDepartment().getName());
        meetingDTO.setRoom(meeting.getRoom().getName());
        meetingDTO.setStatus(meeting.getStatus().getDescription());
        meetingDTO.setPath(meeting.getPath());
        meetingDTO.setRememberCode(meeting.getRememberCode());


        List<MemberByIdDTO> memberDTOs = meeting.getMembers().stream()
                .map(member -> {
                    MemberByIdDTO memberDTO = new MemberByIdDTO();
                    Employee employee = member.getEmployee();
                    Role role = member.getRole();
                    memberDTO.setIdMember(employee.getIdEmployee());
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

        Department department = departmentRepository.findByName(request.getDepartment())
                .orElseThrow(() -> new RuntimeException("Phòng ban không tồn tại: " + request.getDepartment()));


        Room room = roomRepository.findByName(request.getRoom())
                .orElseThrow(() -> new RuntimeException("Phòng họp không tồn tại: " + request.getRoom()));

        boolean isRememberCodeExists = meetingRepository.existsByRememberCode(request.getRememberCode());
        if(isRememberCodeExists) {
            throw new RuntimeException("Mã gợi nhớ đã tồn tại");
        }

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime endTime = null;


        Meeting meeting = new Meeting();
        meeting.setName(request.getName());
        meeting.setRememberCode(request.getRememberCode());
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setRoom(room);
        meeting.setDepartment(department);
        meeting.setStatus(Status.NOT_STARTED);


        try {
            String transcriptFilePath = saveMeetingTranscript(meeting);
            meeting.setTranscript(transcriptFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tạo file transcript: " + e.getMessage(), e);
        }
        return meetingRepository.save(meeting);


    }

    public Status getMeetingStatus(long idMeeting, LocalDateTime startTime, LocalDateTime nowTime) {
        Meeting meetingOptional = meetingRepository.findById(idMeeting).orElseThrow(() -> new RuntimeException("Meeting not found"));
        if (nowTime.plusHours(1).isAfter(startTime) && nowTime.minusHours(1).isBefore(startTime)){
            return Status.UPCOMING;
        }else if ((meetingOptional.getStatus().equals(Status.UPCOMING) || meetingOptional.getStatus().equals(Status.NOT_STARTED)) && nowTime.minusHours(1).isAfter(startTime)){
            return Status.CANCELED;
        }
        return Status.NOT_STARTED;
    }


    @Scheduled(fixedRate = 60000)
    public void updateMeetingStatuses() {
        LocalDateTime nowTime = LocalDateTime.now();
        List<Meeting> meetings = meetingRepository.findAll();
        System.out.println("Chạy");
        for (Meeting meeting : meetings) {
            Status newStatus = getMeetingStatus(meeting.getId(), meeting.getStartTime(), nowTime);
            if (!meeting.getStatus().equals(newStatus)) {
                meeting.setStatus(newStatus);
                meetingRepository.save(meeting);
            }
        }
    }


    public void updateMeetingStatus(long meetingId, Status newStatus) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new RuntimeException("Meeting not found"));
        meeting.setStatus(newStatus);
        meetingRepository.save(meeting);
    }


    private String saveMeetingTranscript(Meeting meeting) throws IOException {
        String meetingFolderName = meeting.getName();
        Path meetingFolderPath = Paths.get("meeting_transcripts", meetingFolderName);
        File meetingFolder = new File(meetingFolderPath.toString());
        if (!meetingFolder.exists()) {
            meetingFolder.mkdirs();
        }

        String transcriptFileName = meeting.getRememberCode() + "_transcript.txt";
        Path transcriptFilePath = meetingFolderPath.resolve(transcriptFileName);

        try (FileWriter fileWriter = new FileWriter(transcriptFilePath.toFile())) {
            fileWriter.write("Transcript for meeting: " + meeting.getName());
        }

        return transcriptFilePath.toString();
    }


    public List<MeetingDTO> getMeetingsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<Employee> currentEmployee = employeeRepository.findByUsername(username);

        boolean isSecretary = currentEmployee.get().getPosition().getName().equals("Thư ký");

        List<Meeting> meetings;

        if (isSecretary) {
            meetings = meetingRepository.findAll();
        } else {
            meetings = meetingRepository.findByMembersEmployee(currentEmployee.get());
        }

        return meetings.stream().map(meeting -> {
            MeetingDTO dto = new MeetingDTO();
            dto.setId(meeting.getId());
            dto.setName(meeting.getName());
            dto.setStartTime(meeting.getStartTime());
            dto.setDepartment(meeting.getDepartment().getName());
            dto.setRoom(meeting.getRoom().getName());
            dto.setStatus(meeting.getStatus().getDescription());
            dto.setRememberCode(meeting.getRememberCode());
            dto.setPath((meeting.getPath()));
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

    public void updateMeeting(Long idMeeting, com.virtual_assistant.meet.dto.request.MeetingDTO meetingDTO) {
        Meeting meeting = meetingRepository.findById(idMeeting)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id " + idMeeting));


        meeting.setName(meetingDTO.getName());
        meeting.setRememberCode(meetingDTO.getRememberCode());
        meeting.setStartTime(meetingDTO.getStartTime());
        Department department = departmentRepository.findByName(meetingDTO.getDepartment())
                .orElseThrow(() -> new RuntimeException("Department not found with id " + meetingDTO.getDepartment()));
        meeting.setDepartment(department);
        Room room = roomRepository.findByName(meetingDTO.getRoom())
                .orElseThrow(() -> new RuntimeException("Room not found with id " + meetingDTO.getRoom()));
        meeting.setRoom(room);
        meetingRepository.save(meeting);
    }

    public void completeMeeting(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc họp với ID: " + meetingId));

        if (meeting.getStatus() == Status.COMPLETED) {
            throw new RuntimeException("Cuộc họp đã được đánh dấu là COMPLETED trước đó.");
        }
        meeting.setEndTime(LocalDateTime.now());
        meeting.setStatus(Status.COMPLETED);


        meetingRepository.save(meeting);
    }






}
