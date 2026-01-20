package com.jobportal.dto;

public class JobSeekerApplicationResponse {
    private final long applicationId;
    private final String role;
    private final String company;
    private final String status;
    private final String appliedAt;

    public JobSeekerApplicationResponse(
            long applicationId,
            String role,
            String company,
            String status,
            String appliedAt
    ) {
        this.applicationId = applicationId;
        this.role = role;
        this.company = company;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public String getRole() {
        return role;
    }

    public String getCompany() {
        return company;
    }

    public String getStatus() {
        return status;
    }

    public String getAppliedAt() {
        return appliedAt;
    }
}
