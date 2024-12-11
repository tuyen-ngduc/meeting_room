package com.virtual_assistant.meet.domain;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Member {
    @EmbeddedId
    private MemberId id;

    @ManyToOne
    @JoinColumn(name = "idMember", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "idMeeting", insertable = false, updatable = false)
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "idRole", insertable = false, updatable = false)
    private Role role;

    public MemberId getId() {
        return id;
    }

    public void setId(MemberId id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}



