package com.github.osinn.task.scheduler.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * triggers表
 *
 * @author wency_cai
 */
@Data
@TableName("qrtz_triggers")
public class TriggersEntity {

    private String triggerName;

    private String jobName;

    private String description;

    private String jobGroup;

    private String triggerState;
}
