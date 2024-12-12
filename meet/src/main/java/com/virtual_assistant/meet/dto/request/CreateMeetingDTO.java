package com.virtual_assistant.meet.dto.request;

import java.time.LocalDateTime;

public class CreateMeetingDTO {
    private String name;
    private String rememberCode;
    private LocalDateTime startTime;
    private String department;
    private String room;

    public CreateMeetingDTO(String name, String rememberCode, LocalDateTime startTime, String department, String room) {
        this.name = name;
        this.rememberCode = rememberCode;
        this.startTime = startTime;
        this.department = department;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRememberCode() {
        return rememberCode;
    }

    public void setRememberCode(String rememberCode) {
        this.rememberCode = rememberCode;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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
}
