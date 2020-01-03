package com.productcamp.demo.model.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Question
 *
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    private String id;

    /**
     * 题目类型，1判断题；2单选题；3多选题
     */
    @Indexed(background = true)
    private Integer qtype;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目选项
     */
    private List<Option> options;

    /**
     * 数据标题
     */
    private String datatitle;
    /**
     * 解析
     */
    private String analysis;

    /**
     * 这题是否答对
     */
    private Boolean right;

    /**
     * 这题答的时长
     */
    private Long duration;

    /**
     * 这题的得分
     */
    private Long points;

    private Date createTime;
    private Date updateTime;
}
