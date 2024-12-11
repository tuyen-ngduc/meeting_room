package com.virtual_assistant.meet.domain;

import jakarta.persistence.*;

@Entity
public class Document {
    @Id
    private long id;
    private String name;
    private String path;

    @ManyToOne
    @JoinColumn(name = "idMeeting", nullable = false)
    private Meeting meeting;

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

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
