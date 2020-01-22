package com.productcamp.demo.model.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Test
 *
 * @version 1.0
 */
@Document(collection = "test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id
    private String id;

    /**
     * 题目列表
     */
    @JsonProperty("questionList")
    private List<Question> questionList;

    /**
     * 试卷总分
     */
    @JsonProperty("totalScore")
    private Long totalScore;

    /**
     * 考试标题
     */
    private String title;

//    /**
//     * 所属产品线，1 2 3 4...
//     */
//    @Indexed(background = true)
//    private Long category;

    /**
     * 考试时长
     */
    @JsonProperty("timeLimit")
    private Long timeLimit;

    /**
     * 非引用文档字段。试卷得分
     */
    private Long score;

    private Date createTime;
    private Date updateTime;
}
