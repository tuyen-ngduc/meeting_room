package com.virtual_assistant.meet.enums;

public enum Status {
    UPCOMING("Sắp tới"),
    ONGOING("Đang diễn ra"),
    COMPLETED("Đã hoàn thành"),

    NOT_STARTED("Chưa bắt đầu");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
