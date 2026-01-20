package com.jobportal.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== SNAPSHOT APPLICANT DETAILS =====
    @Column(nullable = false)
    private String applicantName;

    @Column(nullable = false)
    private String applicantEmail;

    @Column(nullable = false)
    private String applicantPhone;

    @Column(nullable = false)
    private LocalDate applicantBirthDate;

    // ===== RELATIONS =====
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ===== STATUS =====
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    // ===== CV =====
    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private CvFile cvFile;

    @PrePersist
    public void onCreate() {
        this.appliedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ApplicationStatus.APPLIED;
        }
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public LocalDate getApplicantBirthDate() {
        return applicantBirthDate;
    }

    public Job getJob() {
        return job;
    }

    public User getUser() {
        return user;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public CvFile getCvFile() {
        return cvFile;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }

    public void setApplicantBirthDate(LocalDate applicantBirthDate) {
        this.applicantBirthDate = applicantBirthDate;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setCvFile(CvFile cvFile) {
        this.cvFile = cvFile;
    }
}
