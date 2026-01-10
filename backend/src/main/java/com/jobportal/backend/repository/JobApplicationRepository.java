package com.jobportal.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.JobApplication;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // ✔ prevent duplicate applications
    Optional<JobApplication> findByUserIdAndJobId(Long userId, Long jobId);

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    // ✔ REQUIRED for EmployerController
    List<JobApplication> findByJobId(Long jobId);
}
