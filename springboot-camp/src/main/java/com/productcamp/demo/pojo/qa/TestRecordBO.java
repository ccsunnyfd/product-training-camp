package com.productcamp.demo.pojo.qa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * TestRecordBO
 *
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class TestRecordBO {
    private Long score;
    private String testId;
    private String recordId;
}
