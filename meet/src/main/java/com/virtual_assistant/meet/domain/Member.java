package com.virtual_assistant.meet.domain;


import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Member {
    @Id
    private String id;
    @MapsId
    @ManyToOne
    @JoinColumn(name = "idMember")
    private Employee employee;

    @ManyToMany(mappedBy = "members")
    private List<Meeting> meetings;

    @ManyToOne
    @JoinColumn(name = "idRole")
    private Role role;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}



