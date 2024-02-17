package com.example.tasktracker.controller;

import com.example.tasktracker.service.MinioService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class MinioController {
    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile multipartFile){
        try {
            minioService.save(multipartFile);
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new RuntimeException(e);
        }
        return "file upload successful";
    }
    @GetMapping("/{name}")
    public byte[] getFile(@PathVariable String name){
        return minioService.findByName(name);
    }

}
