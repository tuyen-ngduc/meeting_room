package com.virtual_assistant.meet.dto.request;

public class DepartmentDTO {
    private String id;

    private String name;
    private String description;

    // Constructor không tham số
    public DepartmentDTO() {
    }

    // Constructor có tham số
    public DepartmentDTO(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter cho 'name'
    public String getName() {
        return name;
    }

    // Setter cho 'name'
    public void setName(String name) {
        this.name = name;
    }

    // Getter cho 'description'
    public String getDescription() {
        return description;
    }

    // Setter cho 'description'
    public void setDescription(String description) {
        this.description = description;
    }

    // Phương thức `toString` (tùy chọn, hữu ích để debug)
    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


