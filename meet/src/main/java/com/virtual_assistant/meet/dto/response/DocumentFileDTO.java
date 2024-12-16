package com.virtual_assistant.meet.dto.response;

public class DocumentFileDTO {
    private long id;
    private String name;
    private String path;

    private long idMeeting;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setIdMeeting(long idMeeting) {
        this.idMeeting = idMeeting;
    }

    public long getIdMeeting() {
        return idMeeting;
    }
}
