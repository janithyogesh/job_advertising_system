package com.jobportal.backend.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.jobportal.backend.dto.JobDetailResponse;
import com.jobportal.backend.dto.JobListItemResponse;
import com.jobportal.backend.dto.JobListResponse;
import com.jobportal.backend.entity.Job;
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
                        buildJobImageUrl(job.getJobImagePath()),
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

    @GetMapping("/{jobId}")
    public JobDetailResponse getJobDetail(@PathVariable Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        LocalDateTime now = LocalDateTime.now();
        String status = job.getDeadline().isBefore(now) ? "EXPIRED" : "ACTIVE";

        return new JobDetailResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompany(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getSalary(),
                job.getContactEmail(),
                job.getContactPhone(),
                job.getCategory().getName(),
                job.getDeadline(),
                buildJobImageUrl(job.getJobImagePath()),
                status
        );
    }

    private String buildJobImageUrl(String jobImagePath) {
        if (jobImagePath == null || jobImagePath.isBlank()) {
            return null;
        }
        Path fileName = Paths.get(jobImagePath).getFileName();
        if (fileName == null) {
            return null;
        }
        return "/api/files/jobs/" + fileName;
    }

}
