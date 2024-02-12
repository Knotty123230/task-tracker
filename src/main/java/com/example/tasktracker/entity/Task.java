package com.example.tasktracker.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Task {
    @Id
    private UUID id;
    private LocalDateTime createdAt;
    private String name;
    private String description;
    private Status status;


    public Task(UUID id, LocalDateTime createdAt, String name, String description) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.description = description;
    }

    public Task() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(createdAt, task.createdAt) && Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, name, description);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
