package com.jobportal.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.jobportal.backend.dto.ApplicationResponse;
import com.jobportal.backend.entity.JobApplication;
import com.jobportal.backend.repository.JobApplicationRepository;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    private final JobApplicationRepository applicationRepository;

    public EmployerController(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // 🔒 JWT PROTECTED
    @GetMapping("/applications/{jobId}")
    public List<ApplicationResponse> getApplicationsForJob(
            @PathVariable Long jobId) {

        List<JobApplication> applications =
                applicationRepository.findByJobId(jobId);

        return applications.stream()
                .map(app -> new ApplicationResponse(
                        app.getId(),
                        app.getUser().getFullName(),
                        app.getUser().getEmail(),
                        app.getStatus(),
                        app.getAppliedAt()
                ))
                .toList();
    }
}
