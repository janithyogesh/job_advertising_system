package com.jobportal.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.jobportal.backend.dto.JobListItemResponse;
import com.jobportal.backend.dto.JobListResponse;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobStatus;
import com.jobportal.backend.repository.JobRepository;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepository jobRepository;

    public JobController(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // 🌍 Public – list all jobs with filters + pagination
    @GetMapping
    public JobListResponse getPublicJobs(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        String statusFilter = status.toUpperCase();
        if (!List.of("ALL", "ACTIVE", "EXPIRED").contains(statusFilter)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status filter");
        }

        Pageable pageable = PageRequest.of(page, size);
        LocalDateTime now = LocalDateTime.now();

        Page<Job> jobPage = jobRepository.findPublicJobs(categoryId, statusFilter, now, pageable);

        List<JobListItemResponse> jobs = jobPage.getContent().stream()
                .map(job -> new JobListItemResponse(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getEmploymentType(),
                        job.getSalary(),
                        job.getJobImagePath(),
                        job.getCategory().getName(),
                        job.getDeadline(),
                        job.getDeadline().isBefore(now) ? "EXPIRED" : "ACTIVE"
                ))
                .toList();

        return new JobListResponse(
                jobs,
                jobPage.getNumber(),
                jobPage.getTotalPages(),
                jobPage.getTotalElements()
        );
    }

    // 🔒 Employer – close job manually
    @PatchMapping("/{jobId}/close")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String closeJob(@PathVariable Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);

        return "Job closed successfully";
    }
}
