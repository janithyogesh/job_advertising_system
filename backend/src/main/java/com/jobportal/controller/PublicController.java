package com.jobportal.controller;

import com.jobportal.dto.JobListResponse;
import com.jobportal.dto.JobResponse;
import com.jobportal.model.Job;
import com.jobportal.service.PortalService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublicController {
    private final PortalService portalService;

    public PublicController(PortalService portalService) {
        this.portalService = portalService;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(portalService.getCategories());
    }

    @GetMapping("/jobs")
    public ResponseEntity<JobListResponse> listJobs(
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        List<Job> filtered = portalService.getJobsFiltered(status, categoryId);
        int totalPages = (int) Math.ceil((double) filtered.size() / size);
        int start = Math.min(page * size, filtered.size());
        int end = Math.min(start + size, filtered.size());
        List<JobResponse> jobs = filtered.subList(start, end).stream()
                .map(job -> portalService.toJobResponse(
                        job,
                        portalService.resolveStatus(job, "ACTIVE", "EXPIRED")
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JobListResponse(jobs, totalPages));
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<?> getJob(@PathVariable long id) {
        Optional<Job> job = portalService.getJobById(id);
        if (job.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        JobResponse response = portalService.toJobResponse(
                job.get(),
                portalService.resolveStatus(job.get(), "ACTIVE", "EXPIRED")
        );
        return ResponseEntity.ok(response);
    }
}
