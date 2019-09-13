package com.productcamp.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * MinioConfig
 *
 * @version 1.0
 */
@Getter
@Setter
@Component
@Configuration
public class MinioConfig {
    @Value("${minio.baseUrl}")
    private String MINIO_BASE_URL;
    @Value("${minio.accessKey}")
    private String MINIO_ACCESS_KEY;
    @Value("${minio.secretKey}")
    private String MINIO_SECRET_KEY;
    @Value("${minio.bucketName}")
    private String MINIO_BUCKET_NAME;
    @Value("${minio.filePath}")
    private String MINIO_FILE_PATH;
}
