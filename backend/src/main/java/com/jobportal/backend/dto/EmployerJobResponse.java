package com.jobportal.backend.dto;

public class EmployerJobResponse {

    private final Long jobId;
    private final String title;
    private final String company;
    private final String location;
    private final String employmentType;

    public EmployerJobResponse(
            Long jobId,
            String title,
            String company,
            String location,
            String employmentType) {

        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
    }

    public Long getJobId() {
        return jobId;
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
}
