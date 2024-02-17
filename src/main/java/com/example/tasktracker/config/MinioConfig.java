package com.example.tasktracker.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class MinioConfig {

    Logger logger = LoggerFactory.getLogger(MinioConfig.class);
    @Value("${minio.url}")
    private String MINIO_URL;
    @Value("${minio.access.name}")
    private String MINIO_ACCESS_KEY;
    @Value("${minio.access.secret}")
    private String MINIO_SECRET_KEY;
    @Value("${minio.bucket.name}")
    private String BUCKET_NAMES;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(MINIO_URL)
                .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
                .build();
    }

    @Bean
    ApplicationRunner initializeBucket(MinioClient minioClient) {
        return args -> {
            String[] names = BUCKET_NAMES.split(", ");
            for (String name : names) {
                boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
                if (!isExist) {
                    try {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
                        logger.info("Bucket created successfully");
                    } catch (MinioException e) {
                        throw new RuntimeException("Error creating buckets: " + Arrays.toString(names), e);
                    }
                }
            }
        };
    }

}
