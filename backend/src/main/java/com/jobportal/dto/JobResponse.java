package com.jobportal.dto;

import com.jobportal.model.EmploymentType;

public class JobResponse {
    private final long id;
    private final String title;
    private final String description;
    private final String company;
    private final String location;
    private final EmploymentType employmentType;
    private final String salary;
    private final String contactEmail;
    private final String contactPhone;
    private final String categoryName;
    private final String deadline;
    private final String status;
    private final String jobImageUrl;

    public JobResponse(
            long id,
            String title,
            String description,
            String company,
            String location,
            EmploymentType employmentType,
            String salary,
            String contactEmail,
            String contactPhone,
            String categoryName,
            String deadline,
            String status,
            String jobImageUrl
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
        this.categoryName = categoryName;
        this.deadline = deadline;
        this.status = status;
        this.jobImageUrl = jobImageUrl;
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

    public String getCategoryName() {
        return categoryName;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public String getJobImageUrl() {
        return jobImageUrl;
    }
}
