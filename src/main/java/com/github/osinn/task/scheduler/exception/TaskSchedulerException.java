package com.github.osinn.task.scheduler.exception;

import lombok.Getter;

/**
 * 异常类
 *
 * @author wency_cai
 */
@Getter
public class TaskSchedulerException extends RuntimeException {

    /**
     * 异常消息
     */
    private final String exMessage;

    public TaskSchedulerException(String message) {
        super(message);
        this.exMessage = message;
    }
}
