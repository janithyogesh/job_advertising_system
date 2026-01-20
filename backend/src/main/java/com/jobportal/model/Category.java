package com.jobportal.model;

public class Category {
    private final long id;
    private final String name;
    private final boolean active;

    public Category(long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }
}
