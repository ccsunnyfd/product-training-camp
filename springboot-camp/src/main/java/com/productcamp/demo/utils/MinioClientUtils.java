package com.productcamp.demo.utils;

import com.productcamp.demo.config.MinioConfig;
import io.minio.MinioClient;
//import io.minio.Result;
//import io.minio.messages.Item;
//import org.apache.commons.configuration2.PropertiesConfiguration;
import io.minio.messages.Bucket;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//import java.io.IOException;
import java.io.InputStream;
import java.util.List;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;

/**
 * MinioClientUtils
 *
 * @version 1.0
 */
public class MinioClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MinioClientUtils.class);

    private static MinioClientUtils minioClientUtils;

    private MinioClient minioClient;

    private static int RETRY_NUM = 3;

    private static final String bucketPublicPolicy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Resource\":[\"arn:aws:s3:::toto\"],\"Sid\":\"\"},{\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Resource\":[\"arn:aws:s3:::test/*\"],\"Sid\":\"\"}]}";

    public static MinioClientUtils getInstance() {
        if (null != minioClientUtils) {
            return minioClientUtils;
        }
        synchronized (MinioClientUtils.class) {
            if (null == minioClientUtils) {
                minioClientUtils = new MinioClientUtils();
            }
        }
        return minioClientUtils;
    }


    private MinioClientUtils() {
        init();
    }

    private void init() {
//        final Configuration configuration = initConfiguration();
//        String url = configuration.getString("minio.url", StringUtils.EMPTY);
//        String username = configuration.getString("minio.name", StringUtils.EMPTY);
//        String password = configuration.getString("minio.password", StringUtils.EMPTY);
//        String region = configuration.getString("minio.region", StringUtils.EMPTY);
//
//        String baseUrl = minioConfig.getMINIO_BASE_URL();
//        String accessKey = minioConfig.getMINIO_ACCESS_KEY();
//        String secretKey = minioConfig.getMINIO_SECRET_KEY();

//        String bucketName = minioConfig.getMINIO_BUCKET_NAME();
//        String fileHost = minioConfig.getMINIO_FILE_HOST();

        String baseUrl = "http://172.16.70.2:9000";
        String accessKey = "ZWHVQOKYOVTXSU9WH3CW";
        String secretKey = "tuZNErRlYeAafY4zLrnJYKNfE+ZIMIOVDZtqaM1U";

        try {
            if (StringUtils.isNotEmpty(baseUrl) && StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
                minioClient = new MinioClient(baseUrl, accessKey, secretKey, false);
            }
        } catch (Exception e) {
            LOGGER.error("restClient.close occur error", e);
        }

    }

    public boolean createBucketPublic(String bucketName) {
        boolean isCreated;
        try {
            if (minioClient.bucketExists(bucketName)) {
                isCreated = false;
            } else {
                minioClient.makeBucket(bucketName);
                minioClient.setBucketPolicy(bucketName, bucketPublicPolicy);
                isCreated = true;
            }

        } catch (Exception e) {
            isCreated = false;
            LOGGER.error("createBucketPublic", e);
            e.printStackTrace();
        }
        return isCreated;
    }

    public String uploadJpegFile(String bucketName, String minioPath, String jpgFilePath) {
        return uploadFile(bucketName, minioPath, jpgFilePath, MediaType.IMAGE_JPEG_VALUE);
    }

    public String uploadJpegStream(String bucketName, String minioPath, InputStream inputStream) {
        return uploadStream(bucketName, minioPath, inputStream, MediaType.IMAGE_JPEG_VALUE);
    }

    public String uploadStream(String bucketName, String minioFilePath, InputStream inputStream, String mediaType) {
        LOGGER.info("uploadStream for bucketName={} minioFilePath={} inputStream.getclass={}, mediaType={}", bucketName,
                minioFilePath, inputStream.getClass(), mediaType);
        if (StringUtils.isBlank(mediaType)) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        try {
            putObjectWithRetry(bucketName, minioFilePath, inputStream, mediaType);
            return minioClient.getObjectUrl(bucketName, minioFilePath);
        } catch (Exception e) {
            LOGGER.error("uploadStream occur error:", e);
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(String bucketName, String minioFilePath, String localFile, String mediaType) {
        LOGGER.info("uploadFile for bucketName={} minioFilePath={} localFile={}, mediaType={}", bucketName,
                minioFilePath, localFile, mediaType);
        if (StringUtils.isBlank(mediaType)) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        try {
            putObjectWithRetry(bucketName, minioFilePath, localFile, mediaType);
            return minioClient.getObjectUrl(bucketName, minioFilePath);
        } catch (Exception e) {
            LOGGER.error("uploadFile occur error:", e);
            throw new RuntimeException(e);
        }
    }

//    public List<MinioEntity> listFilesSwap(String bucketName, String prefix, boolean recursive) {
//        LOGGER.info("list files for bucketName={} prefix={} recursive={}", bucketName, prefix, recursive);
//        return swapResultToEntityList(minioClient.listObjects(bucketName, prefix, recursive));
//    }

//    public Iterable<Result<Item>> listFiles(String bucketName, String prefix, boolean recursive) {
//        LOGGER.info("list files for bucketName={} prefix={} recursive={}", bucketName, prefix, recursive);
//        return minioClient.listObjects(bucketName, prefix, recursive);
//    }


//    public List<MinioEntity> listFilesByBucketNameSwap(String bucketName) {
//        LOGGER.info("listFilesByBucketName for bucketName={}", bucketName);
//        return swapResultToEntityList(minioClient.listObjects(bucketName, null, true));
//    }

//    public Iterable<Result<Item>> listFilesByBucketName(String bucketName) {
//        LOGGER.info("listFilesByBucketName for bucketName={}", bucketName);
//        return minioClient.listObjects(bucketName, null, true);
//    }

//    public Iterable<Result<Item>> listFilesByBucketAndPrefix(String bucketName, String prefix) {
//        LOGGER.info("listFilesByBucketAndPrefix for bucketName={} and prefix={}", bucketName, prefix);
//        return minioClient.listObjects(bucketName, prefix, true);
//    }
//
//    public List<MinioEntity> listFilesByBucketAndPrefixSwap(String bucketName, String prefix) {
//        LOGGER.info("listFilesByBucketAndPrefix for bucketName={} and prefix={}", bucketName, prefix);
//        return swapResultToEntityList(minioClient.listObjects(bucketName, prefix, true));
//    }

//    private Configuration initConfiguration() {
//        ClassLoader classLoader = MinioClientUtils.class.getClassLoader();
//        if (null == classLoader) {
//            classLoader = Thread.currentThread().getContextClassLoader();
//        }
//
//        Configuration configuration = null;
//        URL resource = classLoader.getResource("minio.properties");
//        if (null == resource) {
//            LOGGER.error("can not find minio.properties");
//            throw new RuntimeException("can not find minio.properties");
//        }
//        try {
//            configuration = new PropertiesConfiguration(resource);
//        } catch (ConfigurationException e) {
//            LOGGER.error("load properties from url={} occur error", resource.toString());
//            throw new RuntimeException("load properties from url=" + resource.toString() + " occur error", e);
//        }
//        return configuration;
//    }

//    private MinioEntity swapResultToEntity(Result<Item> result) {
//        MinioEntity minioEntity = new MinioEntity();
//        try {
//            if (result.get() != null) {
//                Item item = result.get();
//                minioEntity.setObjectName(cleanUrlByRemoveIp(item.objectName()));
//                minioEntity.setDir(item.isDir());
//                minioEntity.setEtag(item.etag());
//                minioEntity.setLastModified(item.lastModified());
//                minioEntity.setSize(item.size());
//                minioEntity.setStorageClass(item.storageClass());
//            }
//        } catch (Exception e) {
//            LOGGER.error("UrlUtils error, e={}", e.getMessage());
//        }
//        return minioEntity;
//    }

//    private List<MinioEntity> swapResultToEntityList(Iterable<Result<Item>> results) {
//        List<MinioEntity> minioEntities = new ArrayList<>();
//        for (Result<Item> result : results) {
//            minioEntities.add(swapResultToEntity(result));
//        }
//        return minioEntities;
//    }

    public void putObjectWithRetry(String bucketName, String objectName, InputStream stream, String contentType) {
        int current = 0;
        boolean isSuccess = false;
        while (!isSuccess && current < RETRY_NUM) {
            try {
                minioClient.putObject(bucketName, objectName, stream, contentType);
                isSuccess = true;
            } catch (Exception e) {
                LOGGER.warn("[minio] putObject stream, ErrorResponseException occur for time =" + current, e);
                current++;
            }
        }
        if (current == RETRY_NUM) {
            LOGGER.error("[minio] putObject, backetName={}, objectName={}, failed finally!");
        }
    }

    public void putObjectWithRetry(String bucketName, String objectName, String fileName, String contentType) {
        int current = 0;
        boolean isSuccess = false;
        while (!isSuccess && current < RETRY_NUM) {
            try {
                minioClient.putObject(bucketName, objectName, fileName, contentType);
                isSuccess = true;
            } catch (Exception e) {
                current++;
                LOGGER.debug("[minio] putObject file, ErrorResponseException occur!");
            }
        }
        if (current == RETRY_NUM) {
            LOGGER.error("[minio] putObject, backetName={}, objectName={}, failed finally!");
        }
    }

    public Boolean removeObject(String bucketName, String objectName) {
        LOGGER.info("removeObject for bucketName={} objectName={}", bucketName, objectName);
        try {
            minioClient.removeObject(bucketName, objectName);
            return true;
        } catch (Exception e) {
            LOGGER.error("removeObject occur error:", e);
            return false;
        }
    }

    public static void main(String[] args) {
        MinioClientUtils minioClientUtils = MinioClientUtils.getInstance();
        String url = "";
        minioClientUtils.createBucketPublic("tote");
        url = minioClientUtils.uploadJpegFile("tote",  "test/t","C:\\Users\\David\\Desktop\\nero.jpg");
        System.out.println(url);
    }
//        MinioClientUtils.getInstance().createBucketPublic("helo");

//        try {
            // Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
//            MinioClient minioClient = new MinioClient("http://172.16.70.2:9000", "ZWHVQOKYOVTXSU9WH3CW", "tuZNErRlYeAafY4zLrnJYKNfE+ZIMIOVDZtqaM1U");
//            MinioClient minioClient = new MinioClient("https://play.min.io", "Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");

            // Check if the bucket already exists.
//            boolean isExist = minioClient.bucketExists("totote");
//            if(isExist) {
//                System.out.println("Bucket already exists.");
//            } else {
//                // Make a new bucket called asiatrip to hold a zip file of photos.
//                minioClient.makeBucket("totote");
//            }
//
//            // Upload the zip file to the bucket with putObject
//            minioClient.putObject("totote","totote.svg", "D:\\rorke\\icon-sprite.svg");
//            String objectUrl = minioClient.getObjectUrl("totote", "totote.svg");
//            System.out.println(objectUrl);
//        } catch(Exception e) {
//            System.out.println("Error occurred: " + e);
//        }
//    }
}
