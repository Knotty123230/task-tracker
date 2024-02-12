package com.example.tasktracker.dto;

import com.example.tasktracker.entity.Status;

import java.util.Objects;

public record TaskRequest(String id, String name, String description, Status status) {
    @Override
    public String toString() {
        return "TaskRequest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskRequest that = (TaskRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}
