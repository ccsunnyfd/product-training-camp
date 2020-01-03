package com.productcamp.demo.model.qa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;


/**
 * UserStatus
 *
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatus {
    /**
     * 用户id
     */
    @Indexed(background = true)
    private Long userId;

    /**
     * 考试状态，1已完成；2进行中；3未开始
     */
    @Indexed(background = true)
    private Integer status;
}
