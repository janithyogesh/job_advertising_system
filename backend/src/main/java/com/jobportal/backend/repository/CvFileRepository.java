package com.jobportal.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.CvFile;

public interface CvFileRepository extends JpaRepository<CvFile, Long> {
}
