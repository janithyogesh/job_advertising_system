package com.jobportal.backend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.entity.CvFile;
import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobApplication;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.CvFileRepository;
import com.jobportal.backend.repository.JobApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/jobseeker/applications")
public class JobApplicationController {

    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final CvFileRepository cvFileRepository;

    private static final String UPLOAD_DIR = "uploads/cv/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "image/png",
            "image/jpeg",
            "image/jpg"
    );

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

    @PostMapping(consumes = "multipart/form-data")
    public String applyForJob(
            @RequestParam Long jobId,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String birthDate,
            @RequestParam MultipartFile cv,
            Authentication authentication) throws IOException {

        // ===== FILE VALIDATION =====
        if (cv.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CV file is required");
        }

        if (!ALLOWED_TYPES.contains(cv.getContentType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CV file type");
        }

        if (cv.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File size exceeds 5MB");
        }

        // ===== USER =====
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // ===== JOB =====
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        // ===== DEADLINE CHECK (FIXED) =====
        if (job.getDeadline().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job deadline has passed");
        }

        // ===== DUPLICATE CHECK =====
        if (applicationRepository.existsByUser_IdAndJob_Id(user.getId(), jobId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already applied for this job");
        }

        // ===== SAVE APPLICATION =====
        LocalDate parsedBirthDate;
        try {
            parsedBirthDate = LocalDate.parse(birthDate);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid birth date");
        }

        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setApplicantName(fullName);
        application.setApplicantEmail(email);
        application.setApplicantPhone(phone);
        application.setApplicantBirthDate(parsedBirthDate);
        application.setStatus(ApplicationStatus.APPLIED);

        applicationRepository.save(application);

        // ===== FILE STORAGE =====
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String sanitizedFilename = Optional.ofNullable(cv.getOriginalFilename())
                .map(name -> Paths.get(name).getFileName().toString())
                .filter(name -> !name.isBlank())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "CV filename is required"
                ));

        String filePath = UPLOAD_DIR
                + application.getId()
                + "_"
                + sanitizedFilename;

        cv.transferTo(new File(filePath));

        // ===== SAVE CV METADATA =====
        CvFile cvFile = new CvFile();
        cvFile.setFileName(cv.getOriginalFilename());
        cvFile.setFileType(cv.getContentType());
        cvFile.setFilePath(filePath);
        cvFile.setFileSize(cv.getSize());
        cvFile.setApplication(application);
        application.setCvFile(cvFile);

        cvFileRepository.save(cvFile);

        return "Application submitted successfully";
    }
}
