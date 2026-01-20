package com.jobportal.backend.dto;

import java.time.LocalDateTime;

public class JobDetailResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final String company;
    private final String location;
    private final String employmentType;
    private final String salary;
    private final String contactEmail;
    private final String contactPhone;
    private final String categoryName;
    private final LocalDateTime deadline;
    private final String jobImageUrl;
    private final String status;

    public JobDetailResponse(
            Long id,
            String title,
            String description,
            String company,
            String location,
            String employmentType,
            String salary,
            String contactEmail,
            String contactPhone,
            String categoryName,
            LocalDateTime deadline,
            String jobImageUrl,
            String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.salary = salary;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.categoryName = categoryName;
        this.deadline = deadline;
        this.jobImageUrl = jobImageUrl;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getSalary() {
        return salary;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public String getJobImageUrl() {
        return jobImageUrl;
    }

    public String getStatus() {
        return status;
    }
}
