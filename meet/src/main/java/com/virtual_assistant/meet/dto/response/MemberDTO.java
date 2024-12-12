package com.virtual_assistant.meet.dto.response;

public class MemberDTO {
    private String idMember;
    private String name;
    private String department;

    public MemberDTO(String idMember, String name, String department) {
        this.idMember = idMember;
        this.name = name;
        this.department = department;
    }
}
