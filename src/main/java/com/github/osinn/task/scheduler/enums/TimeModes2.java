package com.github.osinn.task.scheduler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述
 *
 * @author wency_cai
 */
@Getter
@AllArgsConstructor
public enum TimeModes2 {

    /**
     * 分钟
     */
    MINUTE("分钟"),

    /**
     * 时
     */
    HOUR("时"),

    /**
     * 天数
     */
    DAY("天数"),

    /**
     * 星期几
     */
    WEEK("星期几"),

    /**
     * 月
     */
    MONTH("月"),

    /**
     * 年
     */
    YEAR("年");

    private final String text;
}
