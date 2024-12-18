package com.example.demo.services;

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

    @Value("${file.upload-dir}")
    private String uploadDir;
    public String getUploadDir() {
        return uploadDir;
    }

    public String storeFile(MultipartFile file) throws IOException {
        
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "-" + originalFileName;

        Path filePath = uploadPath.resolve(uniqueFileName);
        file.transferTo(filePath);

        return filePath.toString(); 
    }
}
