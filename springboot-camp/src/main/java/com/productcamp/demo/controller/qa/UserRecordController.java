package com.productcamp.demo.controller.qa;

import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.model.qa.UserRecord;
import com.productcamp.demo.pojo.qa.TestRecordBO;
import com.productcamp.demo.pojo.qa.TestSubmitBO;
import com.productcamp.demo.service.UserInfoService;
import com.productcamp.demo.service.qa.UserRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserRecordController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/userRecord")
@Api(value = "测试的api接口", tags={"测试相关"})
public class UserRecordController {
    private UserRecordService userRecordService;
    private UserInfoService userInfoService;

    @Autowired
    public void setUserRecordService(UserRecordService userRecordService) {
        this.userRecordService = userRecordService;
    }

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("pauseTest")
    @ApiOperation(value = "中途退出考试")
    public Map<String, Object> pauseTest(@RequestBody TestSubmitBO testSubmitBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        try {
            String skey = testSubmitBO.getSkey();
            Long userId = userInfoService.getUserIdBySkey(skey);
            userRecordService.pauseTest(userId, testSubmitBO.getRecordId(), testSubmitBO.getAnswerMap());
            respBean = new RespBean("success", "中途退出考试成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "中途退出考试失败");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    @PostMapping("processSubmittedTest")
    @ApiOperation(value = "处理提交的考卷")
    public Map<String, Object> processSubmittedTest(@RequestBody TestSubmitBO testSubmitBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        TestRecordBO testRecordBO = null;
        try {
            String skey = testSubmitBO.getSkey();
            Long userId = userInfoService.getUserIdBySkey(skey);
            testRecordBO = userRecordService.processSubmittedTest(userId, testSubmitBO.getRecordId(), testSubmitBO.getAnswerMap());
            respBean = new RespBean("success", "处理提交的考卷成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "处理提交的考卷失败");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", testRecordBO);
        return map;
    }

    @GetMapping("getTestRecord")
    @ApiOperation(value = "根据记录id查询当前用户的答题记录")
    public Map<String, Object> getTestRecord(@RequestParam(value = "recordId") String recordId) {
        Map<String, Object> map = new HashMap<>();
        UserRecord testRecord = null;
        RespBean respBean;
        try {
            testRecord = userRecordService.getTestRecord(recordId);
            respBean = new RespBean("success", "查询答题记录成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "查询答题记录失败");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", testRecord);
        return map;
    }

    @GetMapping("getUserRecordList")
    @ApiOperation(value = "查询当前用户的答题记录列表，返回信息包括recordId、title、score、updatedAt")
    public Map<String, Object> getUserRecordList(@RequestParam(value="skey") String skey) {
        Map<String, Object> map = new HashMap<>();
        List<UserRecord> userRecordList = null;
        RespBean respBean;
        try {
            Long userId = userInfoService.getUserIdBySkey(skey);
            userRecordList = userRecordService.getUserRecordList(userId);
            respBean = new RespBean("success", "查询用户记录列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "查询用户记录列表成功失败");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", userRecordList);
        return map;
    }

}
