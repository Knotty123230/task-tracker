package com.example.tasktracker;

import com.example.tasktracker.entity.Status;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.repository.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.UUID;


@SpringBootApplication

public class TaskTrackerApplication {





    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }

}
