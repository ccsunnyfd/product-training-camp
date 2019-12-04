package com.productcamp.demo.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * WatchProgressBO
 *
 * @version 1.0
 */
@Getter
@Setter
public class WatchProgressBO {
    private String skey;
    private Long courseId;
    private Long currentTime;
    private Long duration;
}
