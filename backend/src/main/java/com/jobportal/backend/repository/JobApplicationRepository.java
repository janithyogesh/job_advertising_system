package com.jobportal.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.JobApplication;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    Optional<JobApplication> findByUserIdAndJobId(Long userId, Long jobId);
}
