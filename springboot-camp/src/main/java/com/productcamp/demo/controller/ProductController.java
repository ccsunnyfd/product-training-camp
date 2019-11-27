package com.productcamp.demo.controller;

import com.productcamp.demo.model.Course;
import com.productcamp.demo.model.Example;
import com.productcamp.demo.model.Product;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.pojo.StepFormProductBO;
import com.productcamp.demo.service.CourseService;
import com.productcamp.demo.service.ExampleService;
import com.productcamp.demo.service.ProductService;
import com.productcamp.demo.utils.AliyunOSSUtil;
import com.productcamp.demo.utils.UrlFilterAndExtract;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@Api(value = "产品信息的api接口", tags = {"产品信息相关"})
public class ProductController {
    private ProductService productService;
    private ExampleService exampleService;
    private CourseService courseService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setExampleService(ExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("detail")
    @ApiOperation(value = "根据id获取产品信息")
    public Map<String, Object> getProductById(@RequestParam(value = "prodId") Long prodId) {
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
    public Map<String, Object> addNewProduct(@RequestBody Product product) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;
        Long newId = productService.addNewProduct(product);
        if (newId != null) {
            respBean = new RespBean("success", "添加产品信息成功");
        } else {
            respBean = new RespBean("error", "添加产品信息失败");
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("newId", newId);
        return map;
    }

    @PostMapping("del")
    @ApiOperation(value = "删除产品")
    public Map<String, Object> addNewProduct(@RequestParam(value = "prodId") Long prodId) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;
        Product product = productService.getProductById(prodId);
        List<Example> exampleList = exampleService.getExamplesByProductId(prodId);
        List<Course> courseList = courseService.getCoursesByProductId(prodId);
        List<String> urlNeedDelete = new ArrayList<>();
        urlNeedDelete.add(product.getFavicon());
        urlNeedDelete.add(product.getProdImg());
        exampleList.forEach(x -> urlNeedDelete.addAll(UrlFilterAndExtract.extractUrlFromString(x.getHtmlContent())));
        courseList.forEach(c -> urlNeedDelete.add(c.getVideoTrail()));
        List<String> filteredUrlArray = UrlFilterAndExtract.filterOssUrlArray(urlNeedDelete);
        try {
            // 删除example
            exampleList.forEach(x -> exampleService.delExample(x.getId()));
            // 删除course
            courseList.forEach(c -> courseService.delCourse(c.getId()));
            // 删除product
            productService.delProduct(prodId);
            respBean = new RespBean("success", "删除产品" + product.getName());
            // 删除oss上的图片和视频
            List<String> failedFiles = AliyunOSSUtil.deleteFiles(filteredUrlArray);
            map.put("failedFiles", failedFiles);
        } catch(Exception exception) {
            respBean = new RespBean("failure", "删除操作异常" + exception.getMessage());
        }
        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        return map;
    }

    @PostMapping("addForm")
    @ApiOperation(value = "根据分步式表单添加新的产品信息")
    public Map<String, Object> addNewProductFromForm(@RequestBody StepFormProductBO proBO) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;
        Product newPro = new Product(
                proBO.getId(),
                proBO.getName(),
                proBO.getDescription(),
                proBO.getScenario(),
                proBO.getFavicon(),
                proBO.getIconType(),
                proBO.getProdImg());
        Long newId = productService.addNewProduct(newPro);
        if (newId == null) {
            respBean = new RespBean("error", "添加产品信息失败");
        } else {
            Example[] newExampleList = proBO.getExampleList();
            Boolean exFlag = true;
            for (Example newExample : newExampleList
            ) {
                newExample.setProductId(newId);
                Long newExId = exampleService.addNewExample(newExample);
                if (newExId == null) {
                    respBean = new RespBean("error", "添加应用案例失败");
                    exFlag = false;
                    break;
                }
            }
            if (exFlag) {
                Course[] newCourseList = proBO.getCourseList();
                Boolean courseFlag = true;
                Long chapNum = 1L;
                for (Course newCourse : newCourseList
                ) {
                    newCourse.setProductId(newId);
                    newCourse.setChapterNum(chapNum++);
                    Long newCourseId = courseService.addNewCourse(newCourse);
                    if (newCourseId == null) {
                        respBean = new RespBean("error", "添加课程信息失败");
                        courseFlag = false;
                        break;
                    }
                }
                if (courseFlag) {
                    respBean = new RespBean("success", "添加产品信息成功");
                }
            }
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("newId", newId);
        return map;
    }
}
