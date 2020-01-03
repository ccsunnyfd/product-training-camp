package com.productcamp.demo.model.qa;

import lombok.*;

/**
 * Option
 *
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option {
    /**
     * 每次插入时，前端生成一个uuid用以子文档的key
     */
    private String _id;

    /**
     * 选项类型
     */
    private Integer otype = 1;

    /**
     * 选项内容
     */
    private String text;

    /**
     * 选项图片
     */
    private String img;

    /**
     * 是否正确答案
     */
    private Boolean right;

    /**
     * 用户是否选择
     */
    private Boolean selected;
}
