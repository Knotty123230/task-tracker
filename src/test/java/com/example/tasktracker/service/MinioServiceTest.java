package com.example.tasktracker.service;

import com.example.tasktracker.utils.MinioUtils;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MinioServiceTest {
    @Mock
    private MinioClient minioClient;

    private MinioService minioService;

    @Captor
    private ArgumentCaptor<PutObjectArgs> putObjectArgsCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        minioService = new MinioService(minioClient);
    }

    @Test
    void saveSuccess() throws Exception {

        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        String expectedBucketName = MinioUtils.getBucketName(multipartFile.getContentType());


        minioService.save(multipartFile);


        verify(minioClient, times(1)).putObject(putObjectArgsCaptor.capture());
        PutObjectArgs actualArgs = putObjectArgsCaptor.getValue();

        assertEquals(expectedBucketName, actualArgs.bucket());
        assertEquals("test.txt", actualArgs.object());
        assertEquals(multipartFile.getContentType(), actualArgs.contentType());
    }

    @Test
    void saveThrowsIllegalArgumentExceptionWhenFileNameIsEmpty() {

        MultipartFile multipartFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);


        assertThrows(IllegalArgumentException.class, () -> minioService.save(multipartFile));
    }

    @Test
    void findByNameSuccess() throws Exception {
        // Given
        String fileName = "test.txt";
        byte[] fileContent = "test content".getBytes();

        GetObjectResponse getObjectResponse = Mockito.mock(GetObjectResponse.class);
        when(getObjectResponse.readAllBytes()).thenReturn(fileContent);
        when(minioClient.getObject(any())).thenReturn(getObjectResponse);

        // When
        byte[] result = minioService.findByName(fileName);


        assertArrayEquals(fileContent, result);
    }

    @Test
    void methodSaveThrowsIllegalArgumentException() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("adada", "", "text/plain", new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> minioService.save(mockMultipartFile));
    }
}
