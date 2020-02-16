package com.productcamp.demo.model.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * UserRecord
 *
 * @version 1.0
 */
@Document(collection = "userRecord")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserRecord extends Test {
    /**
     * 关联的试卷id
     */
    @Indexed(background = true)
    @JsonProperty("testId")
    private String testId;

    /**
     * 用户id
     */
    @Indexed(background = true)
    @JsonProperty("userId")
    private Long userId;

    /**
     * 考试状态，false进行中；true已完成
     */
    @Indexed(background = true)
    private Boolean completed;

    /**
     * 试卷得分
     */
    private Long score = 0L;
}
