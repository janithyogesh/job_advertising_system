package com.jobportal.backend.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.entity.JobStatus;
import com.jobportal.backend.entity.User;
import com.jobportal.backend.repository.JobCategoryRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;

@RestController
@RequestMapping("/api/employer/jobs")
public class EmployerJobController {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobCategoryRepository categoryRepository;

    private static final String IMAGE_DIR = "uploads/jobs/";
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/png", "image/jpeg", "image/jpg"
    );

    // ✅ Formatter for <input type="datetime-local" />
    private static final DateTimeFormatter DEADLINE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public EmployerJobController(
            JobRepository jobRepository,
            UserRepository userRepository,
            JobCategoryRepository categoryRepository) {

        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // ================= CREATE JOB =================
    @PostMapping(consumes = "multipart/form-data")
    public Job createJob(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String company,
            @RequestParam String location,
            @RequestParam String employmentType,
            @RequestParam(required = false) String salary,
            @RequestParam String contactEmail,
            @RequestParam String contactPhone,
            @RequestParam Long categoryId,
            @RequestParam String deadline,
            @RequestParam MultipartFile image,
            Authentication authentication
    ) throws IOException {

        System.out.println("🔥 CREATE JOB ENDPOINT HIT");

        // ===== IMAGE VALIDATION =====
        if (image.isEmpty()) {
            throw new RuntimeException("Job image is required");
        }

        if (!ALLOWED_IMAGE_TYPES.contains(image.getContentType())) {
            throw new RuntimeException("Invalid image type");
        }

        // ===== EMPLOYER =====
        User employer = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        // ===== CATEGORY =====
        JobCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.isActive()) {
            throw new RuntimeException("Category is inactive");
        }

        // ===== DEADLINE (FIXED) =====
        LocalDateTime parsedDeadline;
        try {
            parsedDeadline = LocalDateTime.parse(deadline, DEADLINE_FORMATTER);
        } catch (Exception e) {
            throw new RuntimeException("Invalid deadline format");
        }

        if (parsedDeadline.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Deadline must be in the future");
        }

        // ===== SAVE IMAGE =====
        File dir = new File(IMAGE_DIR);
        if (!dir.exists()) dir.mkdirs();

        String imagePath =
                IMAGE_DIR + System.currentTimeMillis() + "_" + image.getOriginalFilename();
        image.transferTo(new File(imagePath));

        // ===== CREATE JOB =====
        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setCompany(company);
        job.setLocation(location);
        job.setEmploymentType(employmentType);
        job.setSalary(salary);
        job.setContactEmail(contactEmail);
        job.setContactPhone(contactPhone);
        job.setDeadline(parsedDeadline);
        job.setJobImagePath(imagePath);
        job.setCategory(category);
        job.setPostedBy(employer);
        job.setStatus(JobStatus.OPEN);

        return jobRepository.save(job);
    }

    // ================= LIST EMPLOYER JOBS =================
    @GetMapping
    public List<Job> getMyJobs(Authentication authentication) {
        return jobRepository.findByPostedBy_Email(authentication.getName());
    }
}
