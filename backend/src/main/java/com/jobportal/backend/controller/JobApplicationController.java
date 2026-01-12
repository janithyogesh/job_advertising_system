package com.jobportal.backend.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.backend.entity.CvFile;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobApplication;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.CvFileRepository;
import com.jobportal.backend.repository.JobApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CvFileRepository cvFileRepository;

    private static final String UPLOAD_DIR = "uploads/cv/";

    public JobApplicationController(
            JobApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            CvFileRepository cvFileRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.cvFileRepository = cvFileRepository;
    }

    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping(value = "/apply", consumes = "multipart/form-data")
    public String applyForJob(
            @RequestParam Long jobId,
            @RequestParam MultipartFile cv,
            Authentication authentication) throws IOException {

        if (!cv.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF files allowed");
        }

        if (cv.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds 5MB");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getDeadline().isBefore(LocalDate.now())) {
            throw new RuntimeException("Job deadline has passed");
        }

        if (applicationRepository
                .findByUser_IdAndJob_Id(user.getId(), jobId)
                .isPresent()) {
            throw new RuntimeException("Already applied");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);

        applicationRepository.save(application);

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String filePath = UPLOAD_DIR + application.getId() + "_" + cv.getOriginalFilename();
        cv.transferTo(new File(filePath));

        CvFile cvFile = new CvFile();
        cvFile.setFileName(cv.getOriginalFilename());
        cvFile.setFilePath(filePath);
        cvFile.setApplication(application);

        cvFileRepository.save(cvFile);

        return "Application submitted with CV successfully";
    }
}
