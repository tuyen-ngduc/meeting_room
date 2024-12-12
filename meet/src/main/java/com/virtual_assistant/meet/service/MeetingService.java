package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Department;
import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.domain.Room;
import com.virtual_assistant.meet.dto.request.CreateMeetingDTO;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import com.virtual_assistant.meet.enums.Status;
import com.virtual_assistant.meet.repository.DepartmentRepository;
import com.virtual_assistant.meet.repository.MeetingRepository;
import com.virtual_assistant.meet.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class MeetingService {
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoomRepository roomRepository;
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


        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = null;


        // Tạo cuộc họp mới
        Meeting meeting = new Meeting();
        meeting.setName(request.getName());
        meeting.setRememberCode(request.getRememberCode());
        meeting.setStartTime(startTime);
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
            }
            else {
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

}
