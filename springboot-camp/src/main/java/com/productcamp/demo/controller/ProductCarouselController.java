package com.productcamp.demo.controller;

import com.productcamp.demo.model.ProductCarousel;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.service.ProductCarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductCarouselController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/productCarousel")
@Api(value = "轮播图的api接口", tags={"轮播图相关"})
public class ProductCarouselController {
    private ProductCarouselService ProductCarouselService;

    @Autowired
    public void setProductCarouselService(ProductCarouselService ProductCarouselService) {
        this.ProductCarouselService = ProductCarouselService;
    }

    @PostMapping("list")
    @ApiOperation(value = "获取所有轮播图信息列表")
    public Map<String, Object> getProductCarousel() {
        Map<String, Object> map = new HashMap<>();
        List<ProductCarousel> res = null;
        RespBean respBean = null;
        try {
            res = ProductCarouselService.getProductCarousel();
            respBean = new RespBean("success", "获取轮播图信息列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取轮播图信息列表失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @PostMapping("add")
    @ApiOperation(value = "添加新的轮播图信息")
    public RespBean addNewCarousel(@RequestBody ProductCarousel ProductCarousel) {
        Long newId = ProductCarouselService.addNewCarousel(ProductCarousel);
        if (newId != null) {
            return new RespBean("success", newId + "");
        } else {
            return new RespBean("error", "添加轮播图失败");
        }
    }
}
