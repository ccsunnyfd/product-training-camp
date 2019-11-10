package com.productcamp.demo.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * MPWXUserBO
 *
 * @version 1.0
 */
@Getter
@Setter
public class MPWXLoginBO {
    private String code;
    private String encryptedData;
    private String iv;
}
