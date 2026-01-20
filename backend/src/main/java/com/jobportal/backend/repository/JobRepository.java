package com.jobportal.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobStatus;

public interface JobRepository extends JpaRepository<Job, Long> {

    // 🌍 Public
    List<Job> findByStatus(JobStatus status);

    // 👨‍💼 Employer
    List<Job> findByPostedBy_Email(String email);
}
