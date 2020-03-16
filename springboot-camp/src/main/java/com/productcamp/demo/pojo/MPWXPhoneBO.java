package com.productcamp.demo.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * MPWXPhoneBO
 *
 * @version 1.0
 */
@Getter
@Setter
public class MPWXPhoneBO {
    private String skey;
    private String encryptedData;
    private String iv;
}
