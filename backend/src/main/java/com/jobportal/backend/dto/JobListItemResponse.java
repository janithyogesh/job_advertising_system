package com.jobportal.backend.dto;

import java.time.LocalDateTime;

public class JobListItemResponse {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String employmentType;
    private String salary;
    private String jobImageUrl;
    private String categoryName;
    private LocalDateTime deadline;
    private String status;

    public JobListItemResponse() {
    }

    public JobListItemResponse(
            Long id,
            String title,
            String company,
            String location,
            String employmentType,
            String salary,
            String jobImageUrl,
            String categoryName,
            LocalDateTime deadline,
            String status) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.salary = salary;
        this.jobImageUrl = jobImageUrl;
        this.categoryName = categoryName;
        this.deadline = deadline;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public String getJobImageUrl() {
        return jobImageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setJobImageUrl(String jobImageUrl) {
        this.jobImageUrl = jobImageUrl;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
