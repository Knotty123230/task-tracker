//package com.example.tasktracker.service;
//
//import com.example.tasktracker.dto.TaskRequest;
//import com.example.tasktracker.dto.TaskResponse;
//import com.example.tasktracker.entity.Status;
//import com.example.tasktracker.entity.Task;
//import com.example.tasktracker.mapper.TaskMapper;
//import com.example.tasktracker.repository.TaskRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class TaskServiceTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private TaskMapper taskMapper;
//
//    @Test
//    void testSaveTask() {
//        TaskRequest taskRequest = new TaskRequest(UUID.randomUUID().toString(), "Test Task", "description", Status.PROGRESS);
//
//        Task task = new Task();
//        task.setId(UUID.fromString(taskRequest.id()));
//        task.setName(taskRequest.name());
//        task.setDescription(taskRequest.description());
//
//        TaskResponse taskResponse = new TaskResponse(task.getId().toString(), task.getName(), "description", LocalDateTime.now().toString(), Status.PROGRESS.toString());
//
//        when(taskMapper.taskRequestToTask(taskRequest)).thenReturn(task);
//        when(taskRepository.save(task)).thenReturn(Mono.just(task));
//        when(taskMapper.taskToTaskResponse(task)).thenReturn(taskResponse);
//
//        TaskService taskService = new TaskService(taskRepository, taskMapper);
//        Mono<TaskResponse> savedTask = taskService.save(taskRequest);
//
//        StepVerifier.create(savedTask)
//                .expectNextMatches(response ->
//                        response.id().equals(task.getId().toString()) &&
//                                response.name().equals(task.getName()) &&
//                                response.description().equals(task.getDescription()))
//                .verifyComplete();
//    }
//
//    @Test
//    void getAllTasks() {
//
//    }
//
//    @Test
//    void updateTask() {
//    }
//
//    @Test
//    void delete() {
//    }
//}
