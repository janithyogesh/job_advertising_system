package com.jobportal.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobApplication;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.JobApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;

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

    @PostMapping("/apply")
    public String applyForJob(
            @RequestParam Long userId,
            @RequestParam Long jobId) {

        // Prevent duplicate application
        if (applicationRepository
                .findByUserIdAndJobId(userId, jobId)
                .isPresent()) {
            throw new RuntimeException("You already applied for this job");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);

        applicationRepository.save(application);

        return "Application submitted successfully";
    }
}
