package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.entity.Status;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public Task save(TaskRequest taskRequest) {
        Task entity = taskMapper.taskRequestToTask(taskRequest);
        entity.setId(UUID.randomUUID());
        entity.setStatus(Status.TODO);
        return taskRepository.save(entity);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();

    }

    @Override
    @Transactional
    public Task updateTask(TaskRequest taskRequest) {
        Optional<Task> optionalTask = taskRepository.findById(UUID.fromString(taskRequest.id()));
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setDescription(taskRequest.description());
            task.setName(taskRequest.name());
            task.setStatus(taskRequest.status());
            return taskRepository.save(optionalTask.orElseThrow());
        }
        throw new NotFoundException("task not found");
    }


    @Override
    public Task delete(String id) {
        Optional<Task> optionalTask = taskRepository.findById(UUID.fromString(id));
        optionalTask.ifPresent(taskRepository::delete);
        return optionalTask.orElseThrow(() -> new NotFoundException("task not found"));
    }

}

