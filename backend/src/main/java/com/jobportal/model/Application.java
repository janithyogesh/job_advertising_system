package com.jobportal.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Application {
    private final long id;
    private final long jobId;
    private final String accountEmail;
    private final String applicantName;
    private final String applicantEmail;
    private final String applicantPhone;
    private final LocalDate applicantBirthDate;
    private final String cvFilePath;
    private final String status;
    private final LocalDateTime appliedAt;

    public Application(
            long id,
            long jobId,
            String accountEmail,
            String applicantName,
            String applicantEmail,
            String applicantPhone,
            LocalDate applicantBirthDate,
            String cvFilePath,
            String status,
            LocalDateTime appliedAt
    ) {
        this.id = id;
        this.jobId = jobId;
        this.accountEmail = accountEmail;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.applicantBirthDate = applicantBirthDate;
        this.cvFilePath = cvFilePath;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public long getId() {
        return id;
    }

    public long getJobId() {
        return jobId;
    }

    public String getAccountEmail() {
        return accountEmail;
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

    public String getCvFilePath() {
        return cvFilePath;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
}
