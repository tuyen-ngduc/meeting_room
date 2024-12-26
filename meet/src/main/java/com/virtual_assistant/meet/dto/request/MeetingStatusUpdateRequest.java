package com.virtual_assistant.meet.dto.request;

import com.virtual_assistant.meet.enums.Status;

public class MeetingStatusUpdateRequest {
    private long idMeeting;
    private Status status;

    public long getIdMeeting() {
        return idMeeting;
    }

    public void setIdMeeting(long idMeeting) {
        this.idMeeting = idMeeting;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

