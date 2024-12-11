package com.virtual_assistant.meet.dto.response;

public class EmployeeDTO {
    private String idEmployee;
    private String name;
    private String department;

    public EmployeeDTO(String idEmployee, String name, String department) {
        this.idEmployee = idEmployee;
        this.name = name;
        this.department = department;
    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
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
