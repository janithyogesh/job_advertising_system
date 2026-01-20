package com.jobportal.backend.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jobportal.backend.entity.CvFile;
import com.jobportal.backend.entity.JobApplication;
import com.jobportal.backend.repository.CvFileRepository;
import com.jobportal.backend.repository.JobApplicationRepository;

@RestController
@RequestMapping("/api/employer/applications")
public class EmployerApplicationController {

    private final JobApplicationRepository applicationRepository;
    private final CvFileRepository cvFileRepository;

    public EmployerApplicationController(
            JobApplicationRepository applicationRepository,
            CvFileRepository cvFileRepository) {
        this.applicationRepository = applicationRepository;
        this.cvFileRepository = cvFileRepository;
    }

    @GetMapping("/{applicationId}/cv")
    public ResponseEntity<Resource> downloadCv(
            @PathVariable Long applicationId,
            Authentication authentication) {

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));

        if (!application.getJob().getPostedBy().getEmail().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        CvFile cvFile = cvFileRepository.findByApplication_Id(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV not found"));

        try {
            Path filePath = Paths.get(cvFile.getFilePath()).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CV file missing");
            }

            String sanitizedFileName = Paths.get(cvFile.getFileName()).getFileName().toString();
            MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sanitizedFileName + "\"")
                    .body(resource);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download CV", ex);
        }
    }
}
