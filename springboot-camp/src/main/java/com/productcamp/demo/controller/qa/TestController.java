package com.productcamp.demo.controller.qa;

import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.model.qa.Question;
import com.productcamp.demo.model.qa.Test;
import com.productcamp.demo.service.qa.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * TestController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/test")
@Api(value = "测试的api接口", tags={"测试相关"})
public class TestController {
    private TestService testService;

    @Autowired
    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("detail")
    @ApiOperation(value = "根据id获取测试信息")
    public Map<String, Object> getTestById(@RequestParam(value = "id") String id) {
        Map<String, Object> map = new HashMap<>();
        Test res = null;
        RespBean respBean;
        try {
            res = testService.getTestById(id);
            respBean = new RespBean("success", "获取测试信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取测试信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @GetMapping("getTestQuestions")
    @ApiOperation(value = "根据id获取试卷信息（不包括答案）")
    public Map<String, Object> getTestQuestions(@RequestParam(value = "id") String id) {
        Map<String, Object> map = new HashMap<>();
        Test res = null;
        RespBean respBean;
        try {
            res = testService.getTestById(id);
            List<Question> questionList = res.getQuestionList();
            for ( Question question: questionList) {
                question.getOptionAndRight().setRightIds(null);
            }
            respBean = new RespBean("success", "获取测试信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取测试信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @GetMapping("search/list")
    @ApiOperation(value = "获取在线考试搜索信息分页")
    public Map<String, Object> getTestByKeywords(@RequestParam(value = "keywords") String keywords,
                                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "9") Integer size) {
        Map<String, Object> map = new HashMap<>();
        List<Test> tests = null;
        RespBean respBean;
        int totalCount = 0;
        try {
            Page<Test> pageTests = testService.getPageTests(keywords == null ? null : keywords.trim(), page, size);
            tests = pageTests.getContent();
            totalCount = (int) pageTests.getTotalElements();
            respBean = new RespBean("success", "获取测试搜索信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取测试搜索信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("totalCount", totalCount);
        map.put("data", tests);
        return map;
    }

    @GetMapping("search/getTests")
    @ApiOperation(value = "根据状态和分类获取在线考试列表")
    public Map<String, Object> getTestsByStatusAndCategory(@RequestParam(value = "status", required=false) Integer status,
                                                          @RequestParam(value = "category", required=false) Long category) {
        Map<String, Object> map = new HashMap<>();
        List<Test> tests = null;
        RespBean respBean;
        try {
            tests = testService.getTestsByStatusAndCategory(status, category);
            respBean = new RespBean("success", "获取在线考试列表成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取在线考试列表失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", tests);
        return map;
    }


    @PostMapping("saveOrUpdateTest")
    @ApiOperation(value = "添加或修改测试")
    public RespBean saveOrUpdateTest(@RequestBody Test test) {
//        System.out.println(test.getQuestionList());
        String newId = testService.saveOrUpdateTest(test);
        if (!StringUtils.isEmpty(newId)) {
            return new RespBean("success", "添加或修改测试成功");
        } else {
            return new RespBean("error", "添加或修改测试失败");
        }
    }

    @PostMapping("del")
    @ApiOperation(value = "删除测试")
    public RespBean delTest(@RequestParam(value = "id") String id) {
        testService.delTest(id);
        return new RespBean("success", "删除测试成功");
    }

}
