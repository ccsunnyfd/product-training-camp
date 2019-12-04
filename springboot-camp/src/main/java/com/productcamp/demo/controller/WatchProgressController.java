package com.productcamp.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.model.WatchProgress;
import com.productcamp.demo.pojo.WatchProgressBO;
import com.productcamp.demo.service.UserInfoService;
import com.productcamp.demo.service.WatchProgressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WatchProgressController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/progress")
@Api(value = "课程观看时长的api接口", tags={"课程观看时长相关"})
public class WatchProgressController {
    private UserInfoService userInfoService;
    private WatchProgressService watchProgressService;

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Autowired
    public void setWatchProgressService(WatchProgressService watchProgressService) {
        this.watchProgressService = watchProgressService;
    }

    @GetMapping("getCurrentTimeListByProdId")
    @ApiOperation(value = "根据用户skey和产品Id获取观看进度列表")
    public Map<String, Object> getCurrentTimeListByProdId(@RequestParam(value = "skey") String skey, @RequestParam(value = "prodId") Long prodId) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        try {
            Long userId = userInfoService.getUserIdBySkey(skey);
            if (userId != null && prodId != null) {
                List<WatchProgress> watchProgressList = watchProgressService.findByUserIdAndProdId(userId, prodId);
                if (watchProgressList.size() != 0) {
                    map.put("data", watchProgressList);
                    respBean = new RespBean("success", "获取观看进度成功");
                } else {
                    respBean = new RespBean("info", "还没有观看进度");
                }
            } else {
                respBean = new RespBean("failure", "获取观看进度失败");
            }
        } catch (Exception e) {
            respBean = new RespBean("failure", "获取观看进度发生异常");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    @GetMapping("getCurrentTimeList")
    @ApiOperation(value = "根据用户skey获取观看进度列表")
    public Map<String, Object> getCurrentTimeList(@RequestParam(value = "skey") String skey) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        try {
            Long userId = userInfoService.getUserIdBySkey(skey);
            if (userId != null) {
                List<WatchProgress> watchProgressList = watchProgressService.findByUserId(userId);
                if (watchProgressList.size() != 0) {
                    map.put("data", watchProgressList);
                    respBean = new RespBean("success", "获取观看进度成功");
                } else {
                    respBean = new RespBean("info", "还没有观看进度");
                }
            } else {
                respBean = new RespBean("failure", "获取当前用户id失败");
            }
        } catch (Exception e) {
            respBean = new RespBean("failure", "获取观看进度发生异常");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    @GetMapping("getCurrentTime")
    @ApiOperation(value = "根据用户skey和课程id获取观看进度")
    public Map<String, Object> getCurrentTime(@RequestParam(value = "skey") String skey, @RequestParam(value = "courseId") Long courseId) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        try {
            Long userId = userInfoService.getUserIdBySkey(skey);
            if (userId != null) {
                WatchProgress watchProgress = watchProgressService.findByUserIdIsAndCourseIdIs(userId, courseId);
                if (watchProgress != null) {
                    map.put("data", watchProgress);
                    respBean = new RespBean("success", "获取观看进度成功");
                } else {
                    respBean = new RespBean("info", "还没有观看进度");
                }
            } else {
                respBean = new RespBean("failure", "获取当前用户id失败");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            respBean = new RespBean("failure", "获取观看进度发生异常");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    @PostMapping("saveCurrentTime")
    @ApiOperation(value = "保存或更新当前用户的观看进度")
    public RespBean addOrUpdateWatchProgress(@RequestBody WatchProgressBO watchProgressBO) {
        String skey = watchProgressBO.getSkey();
        Long userId = userInfoService.getUserIdBySkey(skey);
        if (userId == null) {
            return new RespBean("error", "保存进度时找不到当前用户");
        }
        WatchProgress oldProgress = watchProgressService.findByUserIdIsAndCourseIdIs(userId, watchProgressBO.getCourseId());
        WatchProgress newProgress = new WatchProgress();
        newProgress.setUserId(userId);
        newProgress.setCourseId(watchProgressBO.getCourseId());
        newProgress.setCurrentProgress(watchProgressBO.getCurrentTime());
        newProgress.setDuration(watchProgressBO.getDuration());
        if (oldProgress != null) {
            // 更新
            newProgress.setId(oldProgress.getId());
        }
        Long newId = watchProgressService.saveOrUpdateProgress(newProgress);
        if (newId != null) {
            return new RespBean("success", "保存更新进度成功");
        } else {
            return new RespBean("failure", "保存更新进度失败");
        }
    }
}
