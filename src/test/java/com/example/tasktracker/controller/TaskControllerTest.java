package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.entity.Status;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskMapper taskMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void getAllTasks_Authenticated_ReturnsOk() throws Exception {
        given(taskService.getAllTasks()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/task")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTasks_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/task"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void createTask_Authenticated_ReturnsCreated() throws Exception {
        TaskRequest taskRequest = new TaskRequest("", "New Task","New Task Description", Status.PROGRESS );




        given(taskService.save(any(TaskRequest.class))).willReturn(null);
        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void editTask_Authenticated_ReturnsOk() throws Exception {
        TaskRequest taskRequest = new TaskRequest("", "New Task","New Task Description", Status.PROGRESS );


        mockMvc.perform(put("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteTask_Authenticated_ReturnsOk() throws Exception {
        String taskId = "1";

        mockMvc.perform(delete("/api/task/{id}", taskId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }


}
