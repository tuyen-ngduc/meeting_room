package com.virtual_assistant.meet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.virtual_assistant.meet.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    private String rememberCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    @ManyToOne
    @JoinColumn(name = "department")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

    private Status status;
    private String transcript;
    private String path;

    @OneToMany(mappedBy = "meeting")
    private List<Document> documents;

    @ManyToMany
    @JoinTable(name = "meeting_member",
            joinColumns = @JoinColumn(name = "idMeeting"),
            inverseJoinColumns = @JoinColumn(name = "idMember")

    )
    private Set<Member> members = new HashSet<>();

    public Set<Member> getMembers() {
        return members;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }


}
