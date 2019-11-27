package com.productcamp.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.pojo.PlatformLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * PlatformAuthController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/auth")
@Api(value = "管理平台身份验证的api接口")
public class PlatformAuthController {
    @PostMapping("login")
    @ApiOperation(value = "账号密码登录")
    public Map<String, Object> platformLogin(@RequestBody PlatformLoginBO platformLoginBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean;
        JSONObject data = new JSONObject();
        if (platformLoginBO != null &&
                ("admin".equals(platformLoginBO.getUsername().trim())) &&
                (DigestUtils.md5DigestAsHex("password".getBytes()).equals(platformLoginBO.getPassword().trim()))) {
        data.put("id", 110);
        data.put("name", "admin");
        data.put("username", "admin");
        data.put("password", "");
        data.put("avatar", "https://gw.alipayobjects.com/zos/rmsportal/jZUIxmJycoymBprLOUbT.png");
        data.put("status", 1);
        data.put("telephone", "");
        data.put("lastLoginIp", "114.114.114.114");
        data.put("lastLoginTime", 15348376);
        data.put("creatorId", "admin");
        data.put("createTime", 14971606);
        data.put("deleted", 0);
        data.put("roleId", "admin");
        data.put("lang", "zh-CN");
        data.put("token", "4291d7da9005377ec9aec4a71ea837f");
        map.put("result", data);
        respBean = new RespBean("200", "110");
    } else {
        respBean = new RespBean("401", "账号或密码错误");
    }
        map.put("status",respBean.getStatus());
        map.put("msg",respBean.getMsg());
        return map;
}
}
