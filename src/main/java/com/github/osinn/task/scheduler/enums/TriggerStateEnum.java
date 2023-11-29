package com.github.osinn.task.scheduler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author wency_cai
 */
@Getter
@AllArgsConstructor
public enum TriggerStateEnum {

    /**
     * 空
     */
    NONE("空"),

    /**
     * 正常
     */
    NORMAL("正常"),

    /**
     * 暂停
     */
    PAUSED("暂停"),

    /**
     * 完成
     */
    COMPLETE("完成"),

    /**
     * 错误
     */
    ERROR("错误"),

    /**
     * 阻塞
     */
    BLOCKED("阻塞");

    private final String text;
}
