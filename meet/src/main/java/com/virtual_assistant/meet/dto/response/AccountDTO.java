package com.virtual_assistant.meet.dto.response;

public class AccountDTO {
    private Long id;
    private String username;
    private String password;
    private EmployeeDTO employee;

    public AccountDTO(Long id, String username, String password, EmployeeDTO employee) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }
}