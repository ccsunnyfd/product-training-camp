package com.productcamp.demo.service;

import com.productcamp.demo.utils.MinioClientUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * MinioS3Service
 *
 * @version 1.0
 */
@Service
public class MinioS3Service {
    public String uploadStream(MultipartFile data, String bucketName, String path) {
        String uuid =  UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = uuid + data.getOriginalFilename();
        String filePath = path + "/" + fileName;
        String contentType = data.getContentType();
        try {
            InputStream inputStream = data.getInputStream();
            MinioClientUtils minioClientUtils = MinioClientUtils.getInstance();
            return minioClientUtils.uploadStream(
                    bucketName,
                    filePath,
                    inputStream,
                    contentType
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Boolean removeObject(String bucketName, String objectName) {
        MinioClientUtils minioClientUtils = MinioClientUtils.getInstance();
        return minioClientUtils.removeObject(bucketName, objectName);
    }


}
