package com.jobportal.controller;

import com.jobportal.dto.EmployerApplicationResponse;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;
import com.jobportal.model.UserAccount;
import com.jobportal.service.PortalService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {
    private final PortalService portalService;

    public EmployerController(PortalService portalService) {
        this.portalService = portalService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> getEmployerJobs(Authentication authentication) {
        UserAccount employer = new UserAccount(
                0,
                "",
                authentication.getName(),
                "",
                null
        );
        List<JobResponse> jobs = portalService.getJobsForEmployer(employer).stream()
                .map(job -> portalService.toJobResponse(
                        job,
                        portalService.resolveStatus(job, "OPEN", "CLOSED")
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobs);
    }

    @PostMapping(value = "/jobs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createJob(
            Authentication authentication,
            @Valid @ModelAttribute JobCreateRequest request,
            @RequestPart("image") MultipartFile image
    ) {
        UserAccount employer = new UserAccount(
                0,
                "",
                authentication.getName(),
                "",
                null
        );
        try {
            Job job = portalService.createJob(employer, request, image);
            return ResponseEntity.ok(portalService.toJobResponse(
                    job,
                    portalService.resolveStatus(job, "OPEN", "CLOSED")
            ));
        } catch (IllegalArgumentException | IOException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<?> getApplications(
            Authentication authentication,
            @PathVariable long jobId
    ) {
        UserAccount employer = new UserAccount(
                0,
                "",
                authentication.getName(),
                "",
                null
        );
        Job job = portalService.getJobById(jobId).orElse(null);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            List<EmployerApplicationResponse> applications =
                    portalService.getApplicationsForJob(employer, job);
            return ResponseEntity.ok(applications);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/applications/{applicationId}/cv")
    public ResponseEntity<?> downloadCv(
            Authentication authentication,
            @PathVariable long applicationId
    ) {
        UserAccount employer = new UserAccount(
                0,
                "",
                authentication.getName(),
                "",
                null
        );
        try {
            Path filePath = portalService.getCvForApplication(employer, applicationId);
            byte[] data = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);
            String filename = filePath.getFileName().toString();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IllegalArgumentException | IOException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
