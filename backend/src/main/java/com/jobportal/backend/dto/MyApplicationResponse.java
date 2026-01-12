package com.jobportal.backend.dto;

import java.time.LocalDateTime;

public class MyApplicationResponse {

    private final Long applicationId;
    private final String jobTitle;
    private final String company;
    private final String status;
    private final LocalDateTime appliedAt;

    public MyApplicationResponse(
            Long applicationId,
            String jobTitle,
            String company,
            String status,
            LocalDateTime appliedAt) {

        this.applicationId = applicationId;
        this.jobTitle = jobTitle;
        this.company = company;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}
