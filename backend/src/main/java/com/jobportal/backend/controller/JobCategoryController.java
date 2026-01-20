package com.jobportal.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.repository.JobCategoryRepository;

@RestController
@RequestMapping("/api/admin/categories")
public class JobCategoryController {

    private final JobCategoryRepository categoryRepository;

    public JobCategoryController(JobCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public JobCategory createCategory(@RequestParam String name) {
        JobCategory category = new JobCategory();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @GetMapping
    public List<JobCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PatchMapping("/{id}/toggle")
    public JobCategory toggleCategory(@PathVariable Long id) {
        JobCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setActive(!category.isActive());
        return categoryRepository.save(category);
    }
}
