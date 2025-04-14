package com.example.be12hrimimhrbe.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${project.upload.path}")
    private String uploadDir;

    public String upload(MultipartFile file) {
        if (file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuidName = UUID.randomUUID() + ext;
        Path savePath = Paths.get(uploadDir, uuidName);

        try {
            file.transferTo(savePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }

        return uuidName;
    }
}
