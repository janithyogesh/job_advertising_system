package com.jobportal.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    // 🌍 Public – list all OPEN jobs
    @GetMapping
    public List<Job> getAllOpenJobs() {
        return jobRepository.findByStatus(JobStatus.OPEN);
    }

    // 🔒 Employer – close job manually
    @PreAuthorize("hasRole('EMPLOYER')")
    @PatchMapping("/{jobId}/close")
    public String closeJob(@PathVariable Long jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);

        return "Job closed successfully";
    }
}
