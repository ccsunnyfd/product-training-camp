package com.productcamp.demo.controller;

import com.productcamp.demo.model.Example;
import com.productcamp.demo.model.Product;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.service.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExampleController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/example")
@Api(value = "应用案例的api接口", tags={"应用案例相关"})
public class ExampleController {
    private ExampleService exampleService;

    @Autowired
    public void setExampleService(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("list")
    @ApiOperation(value = "根据产品id获取应用案例信息")
    public Map<String, Object> getExamplesByProdId(@RequestParam(value = "prodId") Long prodId ) {
        Map<String, Object> map = new HashMap<>();
        List<Example> res = null;
        RespBean respBean;
        try {
            res = exampleService.getExamplesByProductId(prodId);
            respBean = new RespBean("success", "获取应用案例成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取应用案例失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @PostMapping("add")
    @ApiOperation(value = "添加新的应用案例")
    public RespBean addNewExample(@RequestBody Example newExample) {
        Long newId = exampleService.addNewExample(newExample);
        if (newId != null) {
            return new RespBean("success", "添加应用案例成功");
        } else {
            return new RespBean("error", "添加应用案例失败");
        }
    }
}
