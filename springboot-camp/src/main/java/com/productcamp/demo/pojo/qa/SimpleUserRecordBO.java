package com.productcamp.demo.pojo.qa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * SimpleUserRecordBO
 *
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class SimpleUserRecordBO {
    private String recordId;
    private String title;
    private Long score;
    private Date updatedAt;
}
