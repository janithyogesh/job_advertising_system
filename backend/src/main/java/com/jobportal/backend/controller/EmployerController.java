package com.jobportal.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.backend.dto.ApplicationResponse;
import com.jobportal.backend.dto.ApplicationStatusUpdateRequest;
import com.jobportal.backend.entity.JobApplication;
import com.jobportal.backend.repository.JobApplicationRepository;

@RestController
@RequestMapping("/api/employer")
@PreAuthorize("hasRole('EMPLOYER')") // 🔒 ALL endpoints protected
public class EmployerController {

    private final JobApplicationRepository applicationRepository;

    public EmployerController(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // ✅ View applications for a job
    @GetMapping("/applications/{jobId}")
    public List<ApplicationResponse> getApplicationsForJob(
            @PathVariable Long jobId) {

        List<JobApplication> applications =
                applicationRepository.findByJob_Id(jobId);

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

    // ✅ Approve / Reject application
    @PatchMapping("/applications/{applicationId}/status")
    public String updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusUpdateRequest request) {

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        String status = request.getStatus();

        if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new RuntimeException("Invalid status value");
        }

        application.setStatus(status);
        applicationRepository.save(application);

        return "Application status updated successfully";
    }
}
