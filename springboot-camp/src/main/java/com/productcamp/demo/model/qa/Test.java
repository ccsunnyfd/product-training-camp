package com.productcamp.demo.model.qa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Test
 *
 * @version 1.0
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {
    @Id
    private String id;

    /**
     * 考试标题
     */
    private String title;

    /**
     * 用户考试状态个性化信息
     */
    private List<UserStatus> userStatusList;

    /**
     * 所属产品线，1 2 3 4...
     */
    @Indexed(background = true)
    private Long category;

    /**
     * 题目Id组成
     */
    private List<Question> questionList;

    /**
     * 考试时长
     */
    private Long timeLimit;

    private Date createTime;
    private Date updateTime;
}
