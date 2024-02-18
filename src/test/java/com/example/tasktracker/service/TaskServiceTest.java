package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.entity.Status;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        TaskRequest taskRequest = new TaskRequest(null, "Sample Task", "decription", Status.TODO);
        Task task = new Task();
        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);

        Task savedTask = new Task();
        savedTask.setId(UUID.randomUUID());
        savedTask.setName("Sample Task");
        savedTask.setStatus(Status.TODO);
        when(taskRepository.save(task)).thenReturn(savedTask);

        Task result = taskService.save(taskRequest);

        verify(taskMapper, times(1)).taskRequestToTask(taskRequest);
        verify(taskRepository, times(1)).save(task);
        assertEquals(savedTask, result);
    }

    @Test
    void testGetAllTasksNotNull() {
        List<Task> mockTasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> result = taskService.getAllTasks();

        assertNotNull(result);
    }

    @Test
    void testGetAllTasksCorrectNumber() {
        List<Task> mockTasks = Arrays.asList(new Task(), new Task());
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllTasksCorrectOrder() {
        Task task = new Task();
        task.setName("Task1");
        Task task1 = new Task();
        task1.setName("Task2");
        List<Task> mockTasks = List.of(task, task1);
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> result = taskService.getAllTasks();
        String name = result.get(0).getName();
        String name1 = result.get(1).getName();

        assertEquals("Task1", result.get(0).getName());
        assertEquals("Task2", result.get(1).getName());
    }

    @Test
    public void testUpdateTaskWithValidTaskRequest() {
        UUID uuid = UUID.randomUUID();
        TaskRequest taskRequest = new TaskRequest(uuid.toString(), "Updated Description", "Updated Name", Status.PROGRESS);
        Task existingTask = new Task();
        existingTask.setId(uuid);
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(existingTask));
        Task task = new Task();
        task.setDescription(taskRequest.description());
        task.setStatus(taskRequest.status());
        task.setId(task.getId());
        task.setName(taskRequest.name());
        when(taskRepository.save(existingTask)).thenReturn(task);

        Task updatedTask = taskService.updateTask(taskRequest);

        assertEquals("Updated Description", updatedTask.getName());
        assertEquals("Updated Name", updatedTask.getDescription());
        assertEquals(Status.PROGRESS, updatedTask.getStatus());
    }

    @Test
    public void testUpdateTaskWithInvalidTaskRequest() {
        TaskRequest taskRequest = new TaskRequest(UUID.randomUUID().toString(), "Updated Description", "Updated Name", Status.PROGRESS);
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(taskRequest));
    }

    @Test
    void testDeleteExistingTask() {
        String taskId = UUID.randomUUID().toString();
        Task task = new Task();
        when(taskRepository.findById(any())).thenReturn(Optional.of(task));

        Task result = taskService.delete(taskId);

        verify(taskRepository, times(1)).delete(task);
        assertEquals(task, result);
    }

    @Test
    void testDeleteNonExistingTask() {
        String taskId = UUID.randomUUID().toString();
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.delete(taskId));
    }

    @Test
    void testFindById_ValidId_ReturnsTask() {

        Task expectedTask = new Task();
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedTask));

        Task result = taskService.findById(UUID.randomUUID().toString());

        assertEquals(expectedTask, result);
    }

    @Test
    public void testFindById_InvalidId_ThrowsException() {
        when(taskRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.findById(UUID.randomUUID().toString()));
    }
}