package com.jobportal.service;

import com.jobportal.dto.EmployerApplicationResponse;
import com.jobportal.dto.JobCreateRequest;
import com.jobportal.dto.JobResponse;
import com.jobportal.dto.JobSeekerApplicationResponse;
import com.jobportal.model.Application;
import com.jobportal.model.Category;
import com.jobportal.model.EmploymentType;
import com.jobportal.model.Job;
import com.jobportal.model.Role;
import com.jobportal.model.UserAccount;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PortalService {
    private final Map<String, UserAccount> usersByEmail = new ConcurrentHashMap<>();
    private final Map<Long, Job> jobs = new ConcurrentHashMap<>();
    private final Map<Long, Application> applications = new ConcurrentHashMap<>();
    private final Map<Long, Category> categories = new ConcurrentHashMap<>();
    private final AtomicLong userId = new AtomicLong(1);
    private final AtomicLong jobId = new AtomicLong(1);
    private final AtomicLong applicationId = new AtomicLong(1);
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;
    private final Path uploadRoot;

    public PortalService(
            FileStorageService fileStorageService,
            PasswordEncoder passwordEncoder,
            @Value("${app.upload-dir}") String uploadDir
    ) {
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
        this.uploadRoot = Path.of(uploadDir).toAbsolutePath().normalize();
        seedData();
    }

    private void seedData() {
        if (!categories.isEmpty()) {
            return;
        }

        categories.put(1L, new Category(1L, "Design", true));
        categories.put(2L, new Category(2L, "Engineering", true));
        categories.put(3L, new Category(3L, "Marketing", true));
        categories.put(4L, new Category(4L, "Customer Success", true));
        categories.put(5L, new Category(5L, "Data", true));

        register("Test Employer", "test1@g.com", "test123", Role.EMPLOYER);
        register("Test Seeker", "seeker1@g.com", "seeker123", Role.JOB_SEEKER);

        createSampleJob(
                "Senior UI/UX Designer",
                "Nimbus Labs",
                "Remote • Europe",
                EmploymentType.FULL_TIME,
                "$85k - $110k",
                "design@nimbuslabs.com",
                "+44 123 555 090",
                1L,
                "Lead product design for fintech experiences.",
                LocalDateTime.now().plusDays(15),
                "test1@g.com"
        );
        createSampleJob(
                "Data Analyst",
                "Brightline Health",
                "Austin, TX",
                EmploymentType.FULL_TIME,
                "$70k - $90k",
                "talent@brightline.com",
                "+1 512 555 200",
                5L,
                "Analyze cohort data and build reporting pipelines.",
                LocalDateTime.now().plusDays(20),
                "test1@g.com"
        );
    }

    private void createSampleJob(
            String title,
            String company,
            String location,
            EmploymentType employmentType,
            String salary,
            String contactEmail,
            String contactPhone,
            long categoryId,
            String description,
            LocalDateTime deadline,
            String employerEmail
    ) {
        Category category = categories.get(categoryId);
        long id = jobId.getAndIncrement();
        Job job = new Job(
                id,
                title,
                description,
                company,
                location,
                employmentType,
                salary,
                contactEmail,
                contactPhone,
                categoryId,
                category != null ? category.getName() : "General",
                deadline,
                null,
                employerEmail,
                LocalDateTime.now()
        );
        jobs.put(id, job);
    }

    public UserAccount register(String fullName, String email, String password, Role role) {
        String normalizedEmail = email.toLowerCase(Locale.ROOT).trim();
        if (usersByEmail.containsKey(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }
        UserAccount user = new UserAccount(
                userId.getAndIncrement(),
                fullName,
                normalizedEmail,
                passwordEncoder.encode(password),
                role
        );
        usersByEmail.put(normalizedEmail, user);
        return user;
    }

    public UserAccount authenticate(String email, String password) {
        String normalizedEmail = email.toLowerCase(Locale.ROOT).trim();
        UserAccount user = usersByEmail.get(normalizedEmail);
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories.values());
    }

    public Job createJob(UserAccount employer, JobCreateRequest request, MultipartFile image) throws IOException {
        Category category = categories.get(request.getCategoryId());
        if (category == null || !category.isActive()) {
            throw new IllegalArgumentException("Invalid category");
        }

        String imagePath = fileStorageService.storeFile(image, "job-images");
        String imageUrl = toPublicUrl(imagePath);

        LocalDateTime deadline = LocalDateTime.parse(request.getDeadline());
        long id = jobId.getAndIncrement();

        Job job = new Job(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getCompany(),
                request.getLocation(),
                request.getEmploymentType(),
                request.getSalary(),
                request.getContactEmail(),
                request.getContactPhone(),
                request.getCategoryId(),
                category.getName(),
                deadline,
                imageUrl,
                employer.getEmail(),
                LocalDateTime.now()
        );
        jobs.put(id, job);
        return job;
    }

    public List<Job> getJobsForEmployer(UserAccount employer) {
        return jobs.values().stream()
                .filter(job -> job.getEmployerEmail().equalsIgnoreCase(employer.getEmail()))
                .sorted(Comparator.comparing(Job::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public Optional<Job> getJobById(long id) {
        return Optional.ofNullable(jobs.get(id));
    }

    public List<Job> getJobsFiltered(String status, Long categoryId) {
        return jobs.values().stream()
                .filter(job -> categoryId == null || Objects.equals(job.getCategoryId(), categoryId))
                .filter(job -> filterByStatus(job, status))
                .sorted(Comparator.comparing(Job::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    private boolean filterByStatus(Job job, String status) {
        if (status == null || status.equalsIgnoreCase("ALL")) {
            return true;
        }
        boolean expired = job.getDeadline().isBefore(LocalDateTime.now());
        if (status.equalsIgnoreCase("ACTIVE")) {
            return !expired;
        }
        if (status.equalsIgnoreCase("EXPIRED")) {
            return expired;
        }
        return true;
    }

    public Application applyToJob(
            UserAccount seeker,
            Job job,
            String fullName,
            String email,
            String phone,
            String birthDate,
            MultipartFile cv
    ) throws IOException {
        if (job.getDeadline().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("This job has expired");
        }

        String cvPath = fileStorageService.storeFile(cv, "cvs");
        long id = applicationId.getAndIncrement();
        Application application = new Application(
                id,
                job.getId(),
                seeker.getEmail(),
                fullName,
                email,
                phone,
                LocalDate.parse(birthDate),
                cvPath,
                "SUBMITTED",
                LocalDateTime.now()
        );
        applications.put(id, application);
        return application;
    }

    public List<EmployerApplicationResponse> getApplicationsForJob(UserAccount employer, Job job) {
        if (!job.getEmployerEmail().equalsIgnoreCase(employer.getEmail())) {
            throw new IllegalArgumentException("Not authorized for this job");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return applications.values().stream()
                .filter(application -> application.getJobId() == job.getId())
                .sorted(Comparator.comparing(Application::getAppliedAt).reversed())
                .map(application -> new EmployerApplicationResponse(
                        application.getId(),
                        application.getApplicantName(),
                        application.getApplicantEmail(),
                        application.getApplicantPhone(),
                        application.getApplicantBirthDate().toString(),
                        application.getStatus(),
                        application.getAppliedAt().format(formatter)
                ))
                .collect(Collectors.toList());
    }

    public Path getCvForApplication(UserAccount employer, long applicationId) {
        Application application = applications.get(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }
        Job job = jobs.get(application.getJobId());
        if (job == null || !job.getEmployerEmail().equalsIgnoreCase(employer.getEmail())) {
            throw new IllegalArgumentException("Not authorized for this application");
        }
        return Path.of(application.getCvFilePath());
    }

    public List<JobSeekerApplicationResponse> getApplicationsForSeeker(UserAccount seeker) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return applications.values().stream()
                .filter(application -> application.getAccountEmail().equalsIgnoreCase(seeker.getEmail()))
                .sorted(Comparator.comparing(Application::getAppliedAt).reversed())
                .map(application -> {
                    Job job = jobs.get(application.getJobId());
                    return new JobSeekerApplicationResponse(
                            application.getId(),
                            job != null ? job.getTitle() : "Unknown role",
                            job != null ? job.getCompany() : "Unknown company",
                            application.getStatus(),
                            application.getAppliedAt().format(formatter)
                    );
                })
                .collect(Collectors.toList());
    }

    public JobResponse toJobResponse(Job job, String statusLabel) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompany(),
                job.getLocation(),
                job.getEmploymentType(),
                job.getSalary(),
                job.getContactEmail(),
                job.getContactPhone(),
                job.getCategoryName(),
                job.getDeadline().toString(),
                statusLabel,
                job.getJobImageUrl()
        );
    }

    public String resolveStatus(Job job, String activeLabel, String expiredLabel) {
        boolean expired = job.getDeadline().isBefore(LocalDateTime.now());
        return expired ? expiredLabel : activeLabel;
    }

    private String toPublicUrl(String filePath) {
        Path absolutePath = Path.of(filePath).toAbsolutePath().normalize();
        Path relativePath = uploadRoot.relativize(absolutePath);
        return "/uploads/" + relativePath.toString().replace("\\", "/");
    }
}
