package com.productcamp.demo.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.productcamp.demo.config.AliyunOSSConfig;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * AliyunOSSUtil
 *
 * @version 1.0
 */
public class AliyunOSSUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AliyunOSSUtil.class);
    private static String FILE_URL;
    private static String bucketName = AliyunOSSConfig.BUCKET_NAME;
    private static String endpoint = AliyunOSSConfig.END_POINT;
    private static String accessKeyId = AliyunOSSConfig.AccessKey_ID;
    private static String accessKeySecret = AliyunOSSConfig.AccessKey_Secret;
    private static String fileHost = AliyunOSSConfig.FILE_HOST;
    private static String ossUrl = AliyunOSSConfig.OSS_URL;


    /**
     * 上传文件。
     *
     * @param file 需要上传的文件路径
     * @return 返回oss对象的"URL"
     */
    public static String upload(File file) {
        // 默认值为：true
//        boolean isImage = true;
        // 判断所要上传的图片是否是图片，图片可以预览，其他文件不提供通过URL预览
//        try {
//            Image image = ImageIO.read(file);
//            isImage = image != null;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        logger.info("------OSS文件上传开始--------" + file.getName());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 判断容器是否存在,不存在就创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            // 设置文件路径和名称
            String fileUrl = fileHost + "/" + (dateStr + "/" + UUID.randomUUID().toString().replace("-", "") + "-" + file.getName());
            FILE_URL = ossUrl + fileUrl;
//            if (isImage) {//如果是图片，则图片的URL为：....
//                FILE_URL = "https://" + bucketName + "." + endpoint + "/" + fileUrl;
//            } else {
//                FILE_URL = "非图片，不可预览。文件路径为：" + fileUrl;
//            }

            // 上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file));
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                logger.info("------OSS文件上传成功------" + fileUrl);
            }
        } catch (OSSException oe) {
            logger.error(oe.getMessage());
        } catch (ClientException ce) {
            logger.error(ce.getErrorMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return FILE_URL;
    }

    /**
     * 上传文件。
     *
     * @param file MultipartFile
     * @return 返回oss对象的"URL"
     */
    public static String uploadMultipartFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String uploadUrl = "";
        try {
            if (filename != null && !"".equals(filename.trim())) {
                File newFile = new File(filename);
                FileOutputStream os = new FileOutputStream(newFile);
                os.write(file.getBytes());
                os.close();
                file.transferTo(newFile);
                // 上传到OSS
                uploadUrl = upload(newFile);
                if ("".equals(uploadUrl)) {
                    logger.info("------上传文件失败------");
                } else {
                    logger.info("------OSS文件上传成功------" + uploadUrl);
                }
            } else {
                logger.info("------接收文件为空------");
            }
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        }
        return uploadUrl;
    }

    /**
     * 删除文件。
     *
     * @param objectName 需要删除的文件名称
     */
    public static void deleteFile(String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 批量删除文件。
     *
     * @param objectNames 需要删除的文件名称列表
     * @return 返回删除失败的图片的文件名，例如abc/efg/123.jpg
     */
    public static List<String> deleteFiles(List<String> objectNames) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withQuiet(true).withKeys(objectNames));
        List<String> deleteFailedObjects = deleteObjectsResult.getDeletedObjects();
        // 关闭OSSClient。
        ossClient.shutdown();
        return deleteFailedObjects;
    }

    /**
     * 通过文件名下载文件
     *
     * @param objectName    要下载的文件名
     * @param localFileName 本地要创建的文件名
     */
    public static void downloadFile(String objectName, String localFileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localFileName));
        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**
     * 列举 test 文件下所有的文件
     */
    public static void listFile() {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 构造ListObjectsRequest请求。
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // 设置prefix参数来获取fun目录下的所有文件。
        listObjectsRequest.setPrefix("test/");
        // 列出文件。
        ObjectListing listing = ossClient.listObjects(listObjectsRequest);
        // 遍历所有文件。
        System.out.println("Objects:");
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }
        // 遍历所有commonPrefix。
        System.out.println("CommonPrefixes:");
        for (String commonPrefix : listing.getCommonPrefixes()) {
            System.out.println(commonPrefix);
        }
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
