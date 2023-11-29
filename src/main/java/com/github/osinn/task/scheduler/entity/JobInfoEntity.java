package com.github.osinn.task.scheduler.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.osinn.task.scheduler.enums.TriggerStateEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 任务信息
 *
 * @author wency_cai
 */
@Data
@TableName("tbl_job_info")
public class JobInfoEntity implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 执行任务全限定类名
     */
    private String jobClassName;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 参数
     */
    private String parameter;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务状态
     */
    private TriggerStateEnum jobStatus;

    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;

    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private String createUserId;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private String updateUserId;

}
