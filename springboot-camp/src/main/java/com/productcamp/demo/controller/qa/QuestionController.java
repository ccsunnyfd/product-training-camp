package com.productcamp.demo.controller.qa;

import com.alibaba.fastjson.JSONObject;
import com.productcamp.demo.model.RespBean;
import com.productcamp.demo.model.qa.Question;
import com.productcamp.demo.service.qa.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QuestionController
 *
 * @version 1.0
 */
@RestController
@RequestMapping(value = "api/question")
@Api(value = "问题的api接口", tags={"问题相关"})
public class QuestionController {
    private QuestionService questionService;

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("detail")
    @ApiOperation(value = "根据id获取题目信息")
    public Map<String, Object> getQuestionById(@RequestParam(value = "id") String id) {
        Map<String, Object> map = new HashMap<>();
        Question res = null;
        RespBean respBean = null;
        try {
            res = questionService.getQuestionById(id);
            respBean = new RespBean("success", "获取题目信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取题目信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", res);
        return map;
    }

    @GetMapping("sample/getSampleListByQtype")
    @ApiOperation(value = "按照题目类型抽样取题")
    public Map<String, Object> getSampleListByQtype(@RequestParam(value = "qtype") Integer qtype,
                                                    @RequestParam(value = "samplesize") Long samplesize) {
        Map<String, Object> map = new HashMap<>();
        List<Document> sampleList = null;
        RespBean respBean = null;
        try {
            sampleList = questionService.getSampleListByQtype(qtype, samplesize);
            respBean = new RespBean("success", "采样成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "采样失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("data", sampleList);
        return map;
    }


    @GetMapping("search/list")
    @ApiOperation(value = "获取题库搜索信息分页")
    public Map<String, Object> getQuestionByKeywords(@RequestParam(value = "keywords") String keywords,
                                                    @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "9") Integer size) {
        Map<String, Object> map = new HashMap<>();
        List<Question> questions = null;
        RespBean respBean = null;
        int totalCount = 0;
        try {
            Page<Question> pageQuestions = questionService.getPageQuestions(keywords == null ? null : keywords.trim(), page, size);
            questions = pageQuestions.getContent();
            totalCount = (int) pageQuestions.getTotalElements();
            respBean = new RespBean("success", "获取题库搜索信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean = new RespBean("failure", "获取题库搜索信息失败");
        }

        map.put("status", respBean.getStatus());
        map.put("msg", respBean.getMsg());
        map.put("totalCount", totalCount);
        map.put("data", questions);
        return map;
    }


    @PostMapping("saveOrUpdateQuestion")
    @ApiOperation(value = "添加或修改题目")
    public RespBean saveOrUpdateQuestion(@RequestBody Question question) {
        String newId = questionService.saveOrUpdateQuestion(question);
        if (newId != null) {
            return new RespBean("success", "添加或修改题目成功");
        } else {
            return new RespBean("error", "添加或修改题目失败");
        }
    }

    @PostMapping("del")
    @ApiOperation(value = "删除题目")
    public RespBean delQuestion(@RequestParam(value = "id") String id) {
        Map<String, Object> map = new HashMap<>();
        RespBean respBean = null;
        questionService.delQuestion(id);
        return new RespBean("success", "删除题目成功");
    }
}
