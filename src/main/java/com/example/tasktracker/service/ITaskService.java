package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.entity.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskService {
    Task save(TaskRequest taskRequest);

    List<Task> getAllTasks();

    Optional<Task> updateTask(TaskRequest taskRequest);

    Task delete(String id);
}
