package com.example.tasktracker.repository;

import com.example.tasktracker.entity.Task;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends ReactiveCrudRepository<Task, UUID> {
}
