package com.productcamp.demo.pojo.qa;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

/**
 * TestSubmitBO
 *
 * @version 1.0
 */
@Getter
@Setter
public class TestSubmitBO {
    private String skey;
    private String testId;
    private String recordId;
    private Map<String, Set<String>> answerMap;
}
