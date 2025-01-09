package com.virtual_assistant.meet.dto.request;

public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;

    // Getter and Setter methods

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}


