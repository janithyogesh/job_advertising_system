package com.jobportal.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobportal.backend.entity.Job;
import com.jobportal.backend.entity.JobStatus;

public interface JobRepository extends JpaRepository<Job, Long> {

    // 🌍 Public
    List<Job> findByStatus(JobStatus status);

    @Query("""
            select j from Job j
            where (:categoryId is null or j.category.id = :categoryId)
              and (
                    :statusFilter = 'ALL'
                    or (:statusFilter = 'ACTIVE' and j.deadline >= :now)
                    or (:statusFilter = 'EXPIRED' and j.deadline < :now)
              )
            """)
    Page<Job> findPublicJobs(
            @Param("categoryId") Long categoryId,
            @Param("statusFilter") String statusFilter,
            @Param("now") LocalDateTime now,
            Pageable pageable
    );

    // 👨‍💼 Employer
    List<Job> findByPostedBy_Email(String email);
}
