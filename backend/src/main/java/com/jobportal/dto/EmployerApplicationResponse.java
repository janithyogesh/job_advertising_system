package com.jobportal.dto;

public class EmployerApplicationResponse {
    private final long applicationId;
    private final String applicantName;
    private final String applicantEmail;
    private final String applicantPhone;
    private final String applicantBirthDate;
    private final String status;
    private final String appliedAt;

    public EmployerApplicationResponse(
            long applicationId,
            String applicantName,
            String applicantEmail,
            String applicantPhone,
            String applicantBirthDate,
            String status,
            String appliedAt
    ) {
        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantBirthDate = applicantBirthDate;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public String getApplicantBirthDate() {
        return applicantBirthDate;
    }

    public String getStatus() {
        return status;
    }

    public String getAppliedAt() {
        return appliedAt;
    }
}
