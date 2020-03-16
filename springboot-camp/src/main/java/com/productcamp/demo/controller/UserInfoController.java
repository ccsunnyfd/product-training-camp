package com.productcamp.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.IdentifiedUser;
import com.productcamp.demo.model.TokenUser;
import com.productcamp.demo.model.UserInfo;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.pojo.MPWXLoginBO;
import com.productcamp.demo.pojo.MPWXPhoneBO;
import com.productcamp.demo.pojo.MPWXTokenBO;
import com.productcamp.demo.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * UserInfoController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/user")
@Api(value = "用户信息的api接口")
public class UserInfoController {
    private UserInfoService userInfoService;

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("bindPhone")
    @ApiOperation(value = "通过手机号绑定实名")
    public Map<String, Object> bindPhone(@RequestBody MPWXPhoneBO mpwxPhoneBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;

        if (mpwxPhoneBO != null &&
                (!"".equals(mpwxPhoneBO.getSkey().trim())) &&
                (!"".equals(mpwxPhoneBO.getEncryptedData().trim())) &&
                (!"".equals(mpwxPhoneBO.getIv().trim()))) {
            IdentifiedUser identifiedUser = userInfoService.bindPhone(mpwxPhoneBO.getSkey(), mpwxPhoneBO.getEncryptedData(), mpwxPhoneBO.getIv());
            if(identifiedUser != null) {
                map.put("data", identifiedUser);
                respBean = new RespBean("201", "手机号绑定实名成功");
            } else {
                respBean = new RespBean("400", "不是备案手机号");
            }
        } else {
            respBean = new RespBean("500", "参数不全");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    @PostMapping("bindToken")
    @ApiOperation(value = "通过令牌绑定实名")
    public Map<String, Object> bindToken(@RequestBody MPWXTokenBO mpwxTokenBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;

        if (mpwxTokenBO != null &&
                (!"".equals(mpwxTokenBO.getSkey().trim())) &&
                (!"".equals(mpwxTokenBO.getToken().trim()))) {
            TokenUser tokenUser = userInfoService.bindToken(mpwxTokenBO.getSkey(), mpwxTokenBO.getToken());
            if(tokenUser != null) {
                map.put("data", tokenUser);
                respBean = new RespBean("201", "令牌绑定实名成功");
            } else {
                respBean = new RespBean("400", "不是备案令牌");
            }
        } else {
            respBean = new RespBean("500", "参数不全");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }


    @PostMapping("wxlogin")
    @ApiOperation(value = "微信登录")
    public Map<String, Object> wxlogin(@RequestBody MPWXLoginBO mpwxLoginBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;

        if (mpwxLoginBO != null &&
                (!"".equals(mpwxLoginBO.getCode().trim())) &&
                (!"".equals(mpwxLoginBO.getEncryptedData().trim())) &&
                (!"".equals(mpwxLoginBO.getIv().trim()))) {
            JSONObject res = userInfoService.wxAuth(mpwxLoginBO.getCode(), mpwxLoginBO.getEncryptedData(), mpwxLoginBO.getIv());
            map.put("data", res);
            respBean = new RespBean("201", "微信登录成功");
        } else {
            respBean = new RespBean("500", "参数不全");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

//    @PostMapping("registOrLogin")
//    @ApiOperation(value = "登录或注册用户")
//    public Map<String, Object> registOrLogin(@RequestBody UserInfo userInfo) {
//        Map<String, Object> map = new HashMap<>();
//        UserInfo res = null;
//        RespBean respBean = null;
//
//        if (userInfo != null &&
//                (!"".equals(userInfo.getUsername().trim())) &&
//                (!"".equals(userInfo.getPassword().trim()))) {
//            // 判断用户是否已注册
//            UserInfo resUser = userInfoService.findByUsername(userInfo.getUsername());
//
//            if (resUser == null) {          //注册用户
//                res = userInfoService.register(userInfo);
//                res.setPassword("");
//                respBean = new RespBean("201", "已注册新用户");
//            } else {                // 用户登录
//                Boolean valid = userInfoService.validation(resUser, userInfo);
//                if (valid) {
//                    res = resUser;
//                    res.setPassword("");
//                    respBean = new RespBean("200", "登录成功");
//                } else {
//                    res = null;
//                    respBean = new RespBean("401", "密码不正确");
//                }
//            }
//        } else {
//            respBean = new RespBean("500", "用户名密码不能为空");
//        }
//
//        map.put("status", respBean.getStatus());
//        map.put("msg", respBean.getMsg());
//        map.put("data", res);
//        return map;
//    }


//    @PostMapping("modifyUserInfo")
//    @ApiOperation(value = "修改用户昵称、生日、性别")
//    public Map<String, Object> modifyUserInfo(@RequestBody UserInfo userInfo) {
//        Map<String, Object> map = new HashMap<>();
//        UserInfo res;
//        RespBean respBean = null;
//
//        UserInfo resUser = userInfoService.modifyUserInfo(userInfo);
//
//        if (resUser == null) {          //没有找到用户
//            res = null;
//            respBean = new RespBean("401", "用户异常");
//        } else {                // 找到用户修改其信息
//
//            res = resUser;
//            respBean = new RespBean("200", "修改成功");
//        }
//
//        map.put("status", respBean.getStatus());
//        map.put("msg", respBean.getMsg());
//        map.put("data", res);
//        return map;
//    }


    @PostMapping("logout")
    @ApiOperation(value = "退出登录")
    public Map<String, Object> logout(@RequestParam(value = "skey") String skey) {
        Map<String, Object> map = new HashMap<>();
        UserInfo res = null;
        RespBean respBean = null;

        respBean = new RespBean("200", "退出登录成功");
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

}
