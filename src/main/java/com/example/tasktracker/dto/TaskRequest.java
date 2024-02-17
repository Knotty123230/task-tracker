package com.example.tasktracker.dto;

import com.example.tasktracker.entity.Status;

public record TaskRequest(String id, String name, String description, Status status) {

}
