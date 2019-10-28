package com.productcamp.demo.pojo;

import com.productcamp.demo.model.Course;
import com.productcamp.demo.model.Example;
import lombok.Getter;
import lombok.Setter;

/**
 * StepFormProductBO
 *
 * @version 1.0
 */
@Getter
@Setter
public class StepFormProductBO {
    private String name;
    private String description;
    private String scenario;
    private String favicon;
    private String iconType;
    private String prodImg;
    private Example[] exampleList;
    private Course[] courseList;
}
