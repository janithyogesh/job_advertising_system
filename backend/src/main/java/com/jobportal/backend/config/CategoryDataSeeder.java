package com.jobportal.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.repository.JobCategoryRepository;

@Configuration
public class CategoryDataSeeder {

    @Bean
    CommandLineRunner seedCategories(JobCategoryRepository categoryRepository) {
        return args -> {

            // ✅ Do nothing if categories already exist
            if (categoryRepository.count() > 0) {
                return;
            }

            List<String> defaultCategories = List.of(
                    "IT",
                    "Finance",
                    "Sales",
                    "Marketing",
                    "Management",
                    "Driving"
            );

            for (String name : defaultCategories) {
                JobCategory category = new JobCategory();
                category.setName(name);
                category.setActive(true);
                categoryRepository.save(category);
            }

            System.out.println("✅ Default job categories seeded");
        };
    }
}
