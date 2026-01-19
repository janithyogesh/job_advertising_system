package com.jobportal.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.repository.JobCategoryRepository;

@RestController
@RequestMapping("/api/categories")
public class PublicCategoryController {

    private final JobCategoryRepository categoryRepository;

    public PublicCategoryController(JobCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 🌍 PUBLIC — ACTIVE categories only
    @GetMapping
    public List<JobCategory> getActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }
}
