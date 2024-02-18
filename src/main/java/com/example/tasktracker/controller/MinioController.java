package com.example.tasktracker.controller;

import com.example.tasktracker.service.FileService;
import com.example.tasktracker.service.MinioService;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Get file", description = "Get file")
    @SecurityRequirement(name = "Bearer Authentication")
    public byte[] getFile(@PathVariable String name) {
        return minioService.findByName(name);
    }


    @PostMapping("/{taskId}")
    @Operation(summary = "Create file", description = "Create file")
    @SecurityRequirement(name = "Bearer Authentication")
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
