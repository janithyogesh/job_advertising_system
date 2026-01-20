package com.jobportal.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final Path uploadRoot;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public String storeFile(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        Path targetDir = uploadRoot.resolve(subDir).normalize();
        Files.createDirectories(targetDir);

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        String fileName = UUID.randomUUID() + extension;
        Path targetPath = targetDir.resolve(fileName).normalize();
        Files.copy(file.getInputStream(), targetPath);

        return targetPath.toString();
    }
}
