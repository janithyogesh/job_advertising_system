package com.jobportal.dto;

import java.util.List;

public class JobListResponse {
    private final List<JobResponse> jobs;
    private final int totalPages;

    public JobListResponse(List<JobResponse> jobs, int totalPages) {
        this.jobs = jobs;
        this.totalPages = totalPages;
    }

    public List<JobResponse> getJobs() {
        return jobs;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
