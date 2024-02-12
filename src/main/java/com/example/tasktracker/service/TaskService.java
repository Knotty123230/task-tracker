package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.entity.Status;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
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
    public Mono<TaskResponse> save(TaskRequest taskRequest) {
        return Mono.from(Mono.just(taskRequest)
                .map(taskMapper::taskRequestToTask)
                .doOnNext(it -> it.setStatus(Status.TODO))
                .flatMap(taskRepository::save)
                .map(taskMapper::taskToTaskResponse));
    }

    @Override
    public Flux<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .switchIfEmpty(Flux.error(new NotFoundException("not found")))
                .flatMap(task -> Mono.just(taskMapper.taskToTaskResponse(task)));
    }

    @Override
    @Transactional
    public Mono<TaskResponse> updateTask(TaskRequest taskRequest) {
        return taskRepository.findById(UUID.fromString(taskRequest.id()))
                .flatMap(task -> {
                    task.setStatus(taskRequest.status());
                    task.setDescription(taskRequest.description());
                    task.setName(taskRequest.name());
                    task.setCreatedAt(LocalDateTime.now());
                    return taskRepository.save(task);
                })
                .switchIfEmpty(Mono.error(new NotFoundException("task not found")))
                .map(taskMapper::taskToTaskResponse);
    }


    @Override
    public Mono<Task> delete(String id) {
        return taskRepository.findById(UUID.fromString(id))
                .switchIfEmpty(Mono.error(new NotFoundException("Task with id " + id + " not found")))
                .flatMap(task -> taskRepository.delete(task).then(Mono.just(task)));

    }

}

