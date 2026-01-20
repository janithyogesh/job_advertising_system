package com.jobportal.controller;

import com.jobportal.dto.JobSeekerApplicationResponse;
import com.jobportal.model.Job;
import com.jobportal.model.UserAccount;
import com.jobportal.service.PortalService;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/jobseeker")
public class JobSeekerController {
    private final PortalService portalService;

    public JobSeekerController(PortalService portalService) {
        this.portalService = portalService;
    }

    @PostMapping(value = "/applications", consumes = "multipart/form-data")
    public ResponseEntity<?> apply(
            Authentication authentication,
            @RequestParam long jobId,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String birthDate,
            @RequestParam("cv") MultipartFile cv
    ) {
        UserAccount seeker = new UserAccount(
                0,
                "",
                authentication.getName(),
                "",
                null
        );
        Job job = portalService.getJobById(jobId).orElse(null);
        if (job == null) {
            return ResponseEntity.badRequest().body("Job not found");
        }
        try {
            portalService.applyToJob(seeker, job, fullName, email, phone, birthDate, cv);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException | IOException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/applications")
    public ResponseEntity<List<JobSeekerApplicationResponse>> listApplications(Authentication authentication) {
        UserAccount seeker = new UserAccount(
                0,
                "",
                authentication.getName(),
                "",
                null
        );
        return ResponseEntity.ok(portalService.getApplicationsForSeeker(seeker));
    }
}
