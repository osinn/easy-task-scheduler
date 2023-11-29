package com.github.osinn.task.scheduler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 操作枚举
 *
 * @author wency_cai
 */
@Getter
@AllArgsConstructor
public enum OperateEnum {

    /**
     * 添加
     */
    ADD("添加"),

    /**
     * 修改
     */
    UPDATE("修改"),

    /**
     * 运行一次
     */
    RUN_ONCE("运行一次"),

    /**
     * 暂停
     */
    PAUSE("暂停"),

    /**
     * 恢复
     */
    RESUME("恢复"),

    /**
     * 删除
     */
    DELETE("删除");

    private final String text;

}
