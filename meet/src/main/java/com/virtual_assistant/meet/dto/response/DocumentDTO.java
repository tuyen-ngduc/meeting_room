package com.virtual_assistant.meet.dto.response;

public class DocumentDTO {
    private String name;
    private String rememberCode;

    private String department;
    private String transcript;


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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }
}
