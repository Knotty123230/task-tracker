package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.entity.Task;

import java.util.List;

public interface ITaskService {
    Task save(TaskRequest taskRequest);

    List<Task> getAllTasks();

    Task updateTask(TaskRequest taskRequest);

    Task delete(String id);

    Task findById(String id);
}
