package com.example.be12hrimimhrbe.global;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LocalImageService {
    @Value("${UPLOAD_PATH}")
    private String defaultUploadPath;

    private String makeDir() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String uploadPath = defaultUploadPath + "/" + date;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return "/" + date;
    }

    public String upload(MultipartFile file) {

        String uploadPath = makeDir();
        String originFilename = file.getOriginalFilename();

        String uploadFilePath = uploadPath + "/" + UUID.randomUUID().toString() + "_" + originFilename;


        File uploadFile = new File(defaultUploadPath+"/"+uploadFilePath);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패: "+ originFilename, e);
        }
        return uploadFilePath;
    }

}