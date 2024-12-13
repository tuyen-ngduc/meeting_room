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

    public String getIdMember() {
        return idMember;
    }

    public void setIdMember(String idMember) {
        this.idMember = idMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
