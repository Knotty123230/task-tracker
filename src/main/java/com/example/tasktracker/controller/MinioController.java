package com.example.tasktracker.controller;

import com.example.tasktracker.service.FileService;
import com.example.tasktracker.service.MinioService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/file/task")
public class MinioController {
    private final MinioService minioService;
    private final FileService fileService;

    public MinioController(MinioService minioService, FileService fileService) {
        this.minioService = minioService;
        this.fileService = fileService;
    }

    @GetMapping("/{name}")
    public byte[] getFile(@PathVariable String name) {
        return minioService.findByName(name);
    }

    @PostMapping("/{taskId}")
    public void save(@PathVariable String taskId, @RequestParam("file") MultipartFile multipartFile) {
        fileService.save(taskId, multipartFile);
        try {
            minioService.save(multipartFile);
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new RuntimeException(e);
        }
    }
}
