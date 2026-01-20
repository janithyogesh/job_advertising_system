package com.jobportal.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicationResponse {

    private final Long applicationId;
    private final String applicantName;
    private final String applicantEmail;
    private final String applicantPhone;
    private final LocalDate applicantBirthDate;
    private final String status;
    private final LocalDateTime appliedAt;

    public ApplicationResponse(
            Long applicationId,
            String applicantName,
            String applicantEmail,
            String applicantPhone,
            LocalDate applicantBirthDate,
            String status,
            LocalDateTime appliedAt) {

        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantBirthDate = applicantBirthDate;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public Long getApplicationId() {
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

    public LocalDate getApplicantBirthDate() {
        return applicantBirthDate;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}
