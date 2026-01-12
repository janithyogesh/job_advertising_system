package com.jobportal.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.backend.dto.MyApplicationResponse;
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

    // 🔒 ONLY JOB SEEKERS CAN APPLY
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/apply")
    public String applyForJob(
            @RequestParam Long jobId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (applicationRepository
                .findByUser_IdAndJob_Id(user.getId(), jobId)
                .isPresent()) {
            throw new RuntimeException("You already applied for this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("APPLIED");

        applicationRepository.save(application);

        return "Application submitted successfully";
    }

    @PreAuthorize("hasRole('JOB_SEEKER')")
    @GetMapping("/my")
    public List<MyApplicationResponse> myApplications(Authentication authentication) {

    String email = authentication.getName();

    return applicationRepository.findByUser_Email(email)
            .stream()
            .map(app -> new MyApplicationResponse(
                    app.getId(),
                    app.getJob().getTitle(),
                    app.getJob().getCompany(),
                    app.getStatus(),
                    app.getAppliedAt()
            ))
            .toList();
}

}
