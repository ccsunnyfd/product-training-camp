package com.productcamp.demo.controller;

import com.productcamp.demo.model.Product;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/product")
@Api(value = "产品信息的api接口", tags={"产品信息相关"})
public class ProductController {
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("detail")
    @ApiOperation(value = "根据id获取产品信息")
    public Map<String, Object> getProductById(@RequestParam(value = "prodId") Long prodId ) {
        Map<String, Object> map = new HashMap<>();
        Product res = null;
        RespBean respBean = null;
        try {
            res = productService.getProductById(prodId);
            respBean = new RespBean("success", "获取产品信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取产品信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @PostMapping("list")
    @ApiOperation(value = "获取产品信息列表")
    public Map<String, Object> getProduct() {
        Map<String, Object> map = new HashMap<>();
        List<Product> res = null;
        RespBean respBean = null;
        try {
            res = productService.getProduct();
            respBean = new RespBean("success", "获取产品信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取产品信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }


    @GetMapping("search/list")
    @ApiOperation(value = "获取产品搜索信息分页")
    public Map<String, Object> getProductByKeywords(@RequestParam(value = "keywords") String keywords,
                                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "9") Integer size) {
        Map<String, Object> map = new HashMap<>();
        List<Product> products = null;
        RespBean respBean = null;
        int totalCount = 0;
        try {
            Page<Product> pageProducts = productService.getPageProducts(keywords == null ? null : keywords.trim(), page, size);
            products = pageProducts.getContent();
            totalCount = (int) pageProducts.getTotalElements();
            respBean = new RespBean("success", "获取产品搜索信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取产品搜索信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("totalCount", totalCount);
        map.put("data", products);
        return map;
    }


    @PostMapping("add")
    @ApiOperation(value = "添加新的产品信息")
    public RespBean addNewProduct(@RequestBody Product product) {
        Long newId = productService.addNewProduct(product);
        if (newId != null) {
            return new RespBean("success", "添加产品信息成功");
        } else {
            return new RespBean("error", "添加产品信息失败");
        }
    }
}
