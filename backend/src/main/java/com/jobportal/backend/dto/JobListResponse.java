package com.jobportal.backend.dto;

import java.util.List;

public class JobListResponse {

    private List<JobListItemResponse> jobs;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public JobListResponse() {
    }

    public JobListResponse(
            List<JobListItemResponse> jobs,
            int currentPage,
            int totalPages,
            long totalElements) {
        this.jobs = jobs;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public List<JobListItemResponse> getJobs() {
        return jobs;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setJobs(List<JobListItemResponse> jobs) {
        this.jobs = jobs;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
