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
     * 选项的id
     */
    private String id;

    /**
     * 选项内容
     */
    private String text;

    /**
     * 选项图片
     */
    private String img;

    /**
     * 非引用文档字段。是否选中
     */
    private Boolean checked;
}
