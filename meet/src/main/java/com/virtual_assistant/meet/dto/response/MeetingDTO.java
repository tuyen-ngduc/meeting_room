package com.virtual_assistant.meet.dto.response;

import com.virtual_assistant.meet.domain.Document;
import com.virtual_assistant.meet.enums.Status;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingDTO {
    private Long id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime expectedEndTime;
    private String department;
    private String room;
    private String status;
    private String rememberCode;

    private String path;
    private List<MemberByIdDTO> members;


    public List<MemberByIdDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberByIdDTO> members) {
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getExpectedEndTime() {
        return expectedEndTime;
    }

    public void setExpectedEndTime(LocalDateTime expectedEndTime) {
        this.expectedEndTime = expectedEndTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRememberCode() {
        return rememberCode;
    }

    public void setRememberCode(String rememberCode) {
        this.rememberCode = rememberCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

