package com.productcamp.demo.controller;

import com.productcamp.demo.config.MinioConfig;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.service.MinioS3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * MinioS3Controller
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/minioS3")
@Api(value = "minioS3的api接口", tags = {"S3相关"})
public class MinioS3Controller {
    private MinioConfig minioConfig;
    private MinioS3Service minioS3Service;

    @Autowired
    public void setMinioConfig(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Autowired
    public void setMinioS3Service(MinioS3Service minioS3Service) {
        this.minioS3Service = minioS3Service;
    }

    @PostMapping("uploadStream")
    @ApiOperation(value = "上传form-data")
    public Map<String, Object> uploadStream(@RequestParam("file") MultipartFile data) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        String bucketName = minioConfig.getMINIO_BUCKET_NAME();
        String path = minioConfig.getMINIO_FILE_PATH();
        String url = minioS3Service.uploadStream(data, bucketName, path);
        if ("".equals(url)) {
            respBean = new RespBean("failure", "上传form-data失败！");
        } else {
            respBean = new RespBean("success", "上传form-data成功！");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("url", url);
        return map;
    }


    @PostMapping("removeObject")
    @ApiOperation(value = "删除对象")
    public Map<String, Object> removeObject(@RequestParam("object") String objectName) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        String bucketName = minioConfig.getMINIO_BUCKET_NAME();
//            try {
//                objectName = java.net.URLDecoder.decode(objectName, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
        Boolean ret = minioS3Service.removeObject(bucketName, objectName);
        if (!ret) {
            respBean = new RespBean("failure", "删除对象失败！");
        } else {
            respBean = new RespBean("success", "删除对象成功！");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

}
