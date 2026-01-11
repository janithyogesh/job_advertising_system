package com.jobportal.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.JobApplication;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    // prevent duplicate applications
    Optional<JobApplication> findByUser_IdAndJob_Id(Long userId, Long jobId);

    boolean existsByUser_IdAndJob_Id(Long userId, Long jobId);

    // FOR EMPLOYER CONTROLLER
    List<JobApplication> findByJob_Id(Long jobId);
}
