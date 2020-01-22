package com.productcamp.demo.model.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * UserRecord
 *
 * @version 1.0
 */
@Document(collection = "userRecord")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecord {
    @Id
    private String id;

    /**
     * 用户id
     */
    @Indexed(background = true)
    @JsonProperty("userId")
    private Long userId;

    /**
     * 记录创建时间
     */
    private Date createdAt;

    /**
     * 记录更新时间
     */
    private Date updatedAt;

    /**
     * 考试历史记录
     */
    @JsonProperty("record")
    private Test record;

//    /**
//     * 考试状态，1已完成；2进行中；3未开始
//     */
//    @Indexed(background = true)
//    private Integer status;
}
