package com.jobportal.backend.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Path JOB_IMAGE_DIR = Paths.get("uploads/jobs").toAbsolutePath().normalize();

    @GetMapping("/jobs/{fileName}")
    public ResponseEntity<Resource> getJobImage(@PathVariable String fileName) {
        try {
            Path requestedPath = JOB_IMAGE_DIR.resolve(fileName).normalize();
            if (!requestedPath.startsWith(JOB_IMAGE_DIR)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            if (!Files.exists(requestedPath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
            }

            Resource resource = new UrlResource(requestedPath.toUri());
            MediaType mediaType = MediaTypeFactory.getMediaType(resource)
                    .orElse(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to load image", ex);
        }
    }
}
