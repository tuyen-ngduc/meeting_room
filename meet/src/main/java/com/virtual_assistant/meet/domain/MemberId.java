package com.virtual_assistant.meet.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MemberId implements Serializable {
    private String idMember;
    private Long idMeeting;
    private Long idRole;

    public MemberId() {

    }
}
