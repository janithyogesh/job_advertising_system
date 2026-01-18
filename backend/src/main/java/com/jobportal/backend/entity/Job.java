package com.jobportal.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== BASIC INFO =====
    @Column(nullable = false)
    private String title;

    @Column(length = 5000, nullable = false)
    private String description;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String employmentType; // FULL_TIME, PART_TIME, CONTRACT

    // ===== OPTIONAL =====
    private String salary;

    // ===== CONTACT =====
    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String contactPhone;

    // ===== DEADLINE =====
    @Column(nullable = false)
    private LocalDateTime deadline;

    // ===== IMAGE =====
    @Column(nullable = false)
    private String jobImagePath;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    // ===== CATEGORY =====
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private JobCategory category;

    // ===== OWNER =====
    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;

    @PrePersist
    public void onCreate() {
        if (status == null) {
            status = JobStatus.OPEN;
        }
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getSalary() {
        return salary;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public String getJobImagePath() {
        return jobImagePath;
    }

    public JobStatus getStatus() {
        return status;
    }

    public JobCategory getCategory() {
        return category;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setJobImagePath(String jobImagePath) {
        this.jobImagePath = jobImagePath;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public void setCategory(JobCategory category) {
        this.category = category;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }
}
