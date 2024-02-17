package com.example.tasktracker.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void save(String taskId, MultipartFile file);
}
