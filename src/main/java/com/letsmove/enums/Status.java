package com.letsmove.enums;

public enum Status {
    NEW("Новый"),
    ACTIVE("Активное"),
    UN_ACTIVE("Не активное"),
    BLOCKED("Заблокирован");

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Status(String name) {
        this.name = name;
    }
}
