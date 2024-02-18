package com.example.tasktracker.service;


import com.example.tasktracker.entity.File;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.repository.FileRepository;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private FileServiceImpl fileService;

    private String taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID().toString();
        task = new Task();
        task.setId(UUID.fromString(taskId));

    }

    @Test
    void save_whenTaskExists_shouldSaveFile() {
        when(file.getOriginalFilename()).thenReturn("testFile.txt");
        when(file.getSize()).thenReturn(1024L);

        // Given
        when(taskRepository.findById(UUID.fromString(taskId))).thenReturn(Optional.of(task));
        when(fileRepository.save(any(File.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        fileService.save(taskId, file);

        // Then
        verify(taskRepository, times(1)).findById(UUID.fromString(taskId));
        verify(fileRepository, times(1)).save(any(File.class));
        verify(taskRepository, times(1)).save(task);
    }


    @Test
    void save_whenTaskDoesNotExist_shouldThrowException() {
        // Given
        when(taskRepository.findById(UUID.fromString(taskId))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> fileService.save(taskId, file));

        verify(taskRepository, times(1)).findById(UUID.fromString(taskId));
    }

}
