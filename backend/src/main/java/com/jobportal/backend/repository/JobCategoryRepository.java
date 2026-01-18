package com.jobportal.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.JobCategory;

public interface JobCategoryRepository extends JpaRepository<JobCategory, Long> {

    Optional<JobCategory> findByName(String name);

    List<JobCategory> findByActiveTrue();
}
