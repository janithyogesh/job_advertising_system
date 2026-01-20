package com.jobportal.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.backend.entity.CvFile;
import java.util.Optional;

public interface CvFileRepository extends JpaRepository<CvFile, Long> {
    Optional<CvFile> findByApplication_Id(Long applicationId);
}
