package com.productcamp.demo.model.qa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OptionAndRight
 *
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionAndRight {
    /**
     * 选项列表
     */
    private List<Option> optionList;

    /**
     * 正确选项的Id列表
     */
    private List<String> rightIds;
}
