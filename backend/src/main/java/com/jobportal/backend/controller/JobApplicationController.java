package com.jobportal.backend.controller;

import java.time.LocalDate;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.jobportal.backend.entity.*;
import com.jobportal.backend.repository.*;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public JobApplicationController(
            JobApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/apply")
    public String applyForJob(
            @RequestParam Long jobId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // 🚫 Deadline enforcement
        if (job.getDeadline() != null &&
            job.getDeadline().isBefore(LocalDate.now())) {

            job.setStatus(JobStatus.EXPIRED);
            jobRepository.save(job);
            throw new RuntimeException("Job deadline has passed");
        }

        if (job.getStatus() != JobStatus.OPEN) {
            throw new RuntimeException("Job is not open for applications");
        }

        if (applicationRepository
                .findByUser_IdAndJob_Id(user.getId(), jobId)
                .isPresent()) {
            throw new RuntimeException("You already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("APPLIED");

        applicationRepository.save(application);

        return "Application submitted successfully";
    }
}
