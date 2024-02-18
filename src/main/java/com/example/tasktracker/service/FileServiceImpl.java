package com.example.tasktracker.service;

import com.example.tasktracker.entity.File;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.repository.FileRepository;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final TaskRepository taskRepository;

    public FileServiceImpl(FileRepository fileRepository, TaskRepository taskRepository) {
        this.fileRepository = fileRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void save(String taskId, MultipartFile file) {
        Task byId = taskRepository.findById(UUID.fromString(taskId)).orElseThrow(() -> new NotFoundException("Task not found with id %s".formatted(taskId)));
        File resultFile = new File();
        resultFile.setId(UUID.randomUUID());
        resultFile.setName(file.getOriginalFilename());
        resultFile.setSize(file.getSize());
        resultFile.setAddedAt(LocalDateTime.now());
        fileRepository.save(resultFile);
        byId.setFile(resultFile);
        taskRepository.save(byId);
    }
}
