package com.jobportal.model;

import java.time.LocalDateTime;

public class Job {
    private final long id;
    private final String title;
    private final String description;
    private final String company;
    private final String location;
    private final EmploymentType employmentType;
    private final String salary;
    private final String contactEmail;
    private final String contactPhone;
    private final long categoryId;
    private final String categoryName;
    private final LocalDateTime deadline;
    private final String jobImageUrl;
    private final String employerEmail;
    private final LocalDateTime createdAt;

    public Job(
            long id,
            String title,
            String description,
            String company,
            String location,
            EmploymentType employmentType,
            String salary,
            String contactEmail,
            String contactPhone,
            long categoryId,
            String categoryName,
            LocalDateTime deadline,
            String jobImageUrl,
            String employerEmail,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.salary = salary;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.deadline = deadline;
        this.jobImageUrl = jobImageUrl;
        this.employerEmail = employerEmail;
        this.createdAt = createdAt;
    }

    public long getId() {
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

    public EmploymentType getEmploymentType() {
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

    public long getCategoryId() {
        return categoryId;
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

    public String getEmployerEmail() {
        return employerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
