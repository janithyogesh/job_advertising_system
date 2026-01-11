package com.jobportal.backend.dto;

public class ApplicationStatusRequest {

    private String status; // APPROVED or REJECTED

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
