package com.productcamp.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.utils.AliyunOSSUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AliyunOSSController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/oss")
@Api(value = "oss的api接口", tags = {"oss相关"})
public class AliyunOSSController {
//    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 测试上传文件到阿里云OSS存储
     *
     * @return success
     */
    @RequestMapping("testUpload")
    @ResponseBody
    public String testUpload() {
        File file = new File("C:/Users/David/Desktop/1.jpg");
        String url = AliyunOSSUtil.upload(file);
        System.out.println(url);
        return "success";
    }

    /**
     * 通过文件名下载文件
     */
    @RequestMapping("testDownload")
    @ResponseBody
    public String testDownload() {
        AliyunOSSUtil.downloadFile(
                "test/2019-11-10/kylin.png", "C:/Users/David/Desktop/downloaded_1.jpg");
        return "success";
    }

    /**
     * 列出某个文件夹下的所有文件
     */
    @RequestMapping("testListFile")
    @ResponseBody
    public String testListFile() {
        AliyunOSSUtil.listFile();
        return "success";
    }

    /**
     * 从阿里云OSS存储删除文件
     */
    @RequestMapping("testDeleteFile")
    @ResponseBody
    public String testDeleteFile() {
        AliyunOSSUtil.deleteFile("test/2019-11-11/d40e2e2e3ce943c2af97ebfde1a3dc1c-blob");
        return "success";
    }

    /**
     * 文件上传（供前端调用）
     */
    @RequestMapping("uploadFile")
    @ApiOperation(value = "上传文件")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        String uploadUrl = AliyunOSSUtil.uploadMultipartFile(file);
        if ("".equals(uploadUrl)) {
            respBean = new RespBean("failure", "上传文件失败！");
        } else {
            respBean = new RespBean("success", "上传文件成功！");
            map.put("url", uploadUrl);
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    /**
     * 批量文件上传（供前端调用）
     */
    @PostMapping("uploadVideos")
    @ApiOperation(value = "批量上传视频文件")
    public Map<String, Object> uploadVideos(@RequestParam("files") List<MultipartFile> files) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;
        List<JSONObject> videoList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                String uploadUrl = AliyunOSSUtil.uploadMultipartFile(file);
                if ("".equals(uploadUrl)) {
                    respBean = new RespBean("failure", "上传文件异常！");
                } else {
                    JSONObject object = new JSONObject();
                    int ext = filename.lastIndexOf(".");
                    object.put("title", filename.substring(0, ext));
                    object.put("videoTrail", uploadUrl);
                    videoList.add(object);
                }
            }
            if (respBean == null) {
                respBean = new RespBean("success", "上传文件成功！");
            }
        } else {
            respBean = new RespBean("failure", "接收文件为空！");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("videoList", videoList);
        return map;
    }

    /**
     * 文件删除（供前端调用）
     */
    @PostMapping("removeFile")
    @ApiOperation(value = "删除对象")
    public Map<String, Object> removeFile(@RequestParam("file") String objectName) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        if (objectName != null) {
            if (!"".equals(objectName.trim())) {
                AliyunOSSUtil.deleteFile(objectName);
                respBean = new RespBean("success", "已发送删除请求！");
            } else {
                respBean = new RespBean("failure", "接收参数为空！");
            }
        } else {
            respBean = new RespBean("failure", "缺少接收参数！");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    /**
     * 批量文件删除（供前端调用）
     */
    @PostMapping("removeFiles")
    @ApiOperation(value = "删除批量对象")
    public Map<String, Object> removeFiles(@RequestParam("files") List<String> objectNames) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        if (objectNames != null) {
            if (objectNames.size() > 0) {
                List<String> failedFiles = AliyunOSSUtil.deleteFiles(objectNames);
                if (failedFiles.size() == 0) {
                    respBean = new RespBean("success", "已删除全部请求对象！");
                } else {
                    respBean = new RespBean("failure", "部分对象删除失败！");
                    map.put("failedFiles", failedFiles);
                }
            } else {
                respBean = new RespBean("failure", "接收参数为空！");
            }
        } else {
            respBean = new RespBean("failure", "缺少接收参数！");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

}
