package com.example.tasktracker.controller;

import com.example.tasktracker.service.FileService;
import com.example.tasktracker.service.MinioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MinioController.class)
class MinioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinioService minioService;

    @MockBean
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void getFileTest() throws Exception {
        String fileName = "testFile.txt";
        byte[] content = "file content".getBytes();


        when(minioService.findByName(fileName)).thenReturn(content);

        mockMvc.perform(get("/api/file/task/{name}", fileName))
                .andExpect(status().isOk())
                .andExpect(content().bytes(content));

        verify(minioService, times(1)).findByName(fileName);
    }

    @Test
    @WithMockUser
    void saveFileTest() throws Exception {
        String taskId = "1";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.txt",
                "text/plain",
                "some xml".getBytes()
        );


        doNothing().when(fileService).save(anyString(), any());
        doNothing().when(minioService).save(any());

        mockMvc.perform(multipart("/api/file/task/{taskId}", taskId)
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(fileService, times(1)).save(eq(taskId), any());
        verify(minioService, times(1)).save(any());
    }
}
