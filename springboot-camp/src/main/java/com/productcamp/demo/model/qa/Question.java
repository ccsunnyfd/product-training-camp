package com.productcamp.demo.model.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Question
 *
 * @version 1.0
 */
@Document(collection = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    private String id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目类型，1判断题；2单选题；3多选题
     */
    @Indexed(background = true)
    @JsonProperty("qType")
    private Integer qType;

    /**
     * 选项和正确勾选
     */
    @JsonProperty("optionAndRight")
    private OptionAndRight optionAndRight;

    /**
     * 解析
     */
    private String analysis;

    /**
     * 这题的得分
     */
    private Long points;

    /**
     * 非引用文档字段。本题得分
     */
    @JsonProperty("getPoints")
    private Long getPoints;

    private Date createTime;
    private Date updateTime;
}
