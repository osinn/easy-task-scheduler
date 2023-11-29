package com.github.osinn.task.scheduler.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.osinn.task.scheduler.entity.JobInfoEntity;
import com.github.osinn.task.scheduler.enums.TriggerStateEnum;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.*;

/**
 * 任务调度管理类
 *
 * @author wency_cai
 */
@Slf4j
public class QuartzManager {

    private static Scheduler scheduler;

    private static final String KEY = "jobClassName";

    public static void setScheduler(Scheduler schedule) {
        QuartzManager.scheduler = schedule;
    }

    public static Scheduler getScheduler() {
        return QuartzManager.scheduler;
    }

    /**
     * 创建定时任务，创建定时任务后默认为启动状态
     *
     * @param jobInfoEntity 定时任务信息类
     */
    public static void createScheduleJob(JobInfoEntity jobInfoEntity) throws Exception {
        //job类的具体实现类
        Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(jobInfoEntity.getJobClassName());
        //构建定时任务信息
        JobKey jobKey = JobKey.jobKey(jobInfoEntity.getJobName(), jobInfoEntity.getJobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfoEntity.getJobName(), jobInfoEntity.getJobGroup());
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey).build();
        //设置定时任务的执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfoEntity.getCronExpression());
        //构建触发器trigger
        CronTrigger trigger;
        if (jobInfoEntity.getStartTime() == null || DateTaskSchedulerUtils.localDateTimeToDate(jobInfoEntity.getStartTime()).getTime() <= new Date().getTime()) {
            trigger = TriggerBuilder.newTrigger()
                    .startNow()
                    .withIdentity(triggerKey)
                    .withDescription(jobInfoEntity.getDescription())
                    .usingJobData(KEY, jobInfoEntity.getJobClassName())
                    .withSchedule(scheduleBuilder)
                    .endAt(DateTaskSchedulerUtils.localDateTimeToDate(jobInfoEntity.getEndTime()))
                    .build();
        } else {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder)
                    .startAt(DateTaskSchedulerUtils.localDateTimeToDate(jobInfoEntity.getStartTime()))
                    .endAt(DateTaskSchedulerUtils.localDateTimeToDate(jobInfoEntity.getEndTime()))
                    .withDescription(jobInfoEntity.getDescription())
                    .usingJobData(KEY, jobInfoEntity.getJobClassName())
                    .build();
        }
        // 写入参数
        if (!StrUtil.isBlank(jobInfoEntity.getParameter())) {
            Map<String, Object> map = JSONUtil.parseObj(jobInfoEntity.getParameter());
            jobDetail.getJobDataMap().putAll(map);
        }
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 暂停定时任务
     *
     * @param jobName 定时任务名称
     */
    public static void pauseScheduleJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 恢复定时任务
     *
     * @param jobName 任务名称
     */
    public static void resumeScheduleJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 根据任务名称运行一次定时任务
     *
     * @param jobName 任务名称
     */
    public static void runOnce(String jobName, String jobGroup) throws SchedulerException {
        scheduler.triggerJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 更新定时任务
     *
     * @param jobInfoEntity 定时任务类
     */
    public static void updateScheduleJob(JobInfoEntity jobInfoEntity) throws SchedulerException, ClassNotFoundException {
        //获取对应任务的触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(jobInfoEntity.getJobName(), jobInfoEntity.getJobGroup());
        //设置定时任务执行方式
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobInfoEntity.getCronExpression());
        //重新构建定时任务的触发器
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        cronTrigger = cronTrigger.getTriggerBuilder()
                .withIdentity(triggerKey)
                .withSchedule(scheduleBuilder)
                .withDescription(jobInfoEntity.getDescription())
                .usingJobData(KEY, jobInfoEntity.getJobClassName())
                .startAt(DateTaskSchedulerUtils.localDateTimeToDate(jobInfoEntity.getStartTime()))
                .endAt(DateTaskSchedulerUtils.localDateTimeToDate(jobInfoEntity.getEndTime())).build();

        //写入参数
        JobKey jobKey = JobKey.jobKey(jobInfoEntity.getJobName(), jobInfoEntity.getJobGroup());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (!StrUtil.isBlank(jobInfoEntity.getParameter())) {
            Map<String, Object> map = JSONUtil.parseObj(jobInfoEntity.getParameter());
            jobDetail.getJobDataMap().putAll(map);
        }
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    /**
     * 删除定时任务
     *
     * @param jobName 任务名称
     */
    public static void deleteScheduleJob(String jobName, String jobGroup) throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }

    /**
     * 查询所有定时任务
     */
    public static List<JobInfoEntity> findAll(String jobGroup) throws SchedulerException {
        GroupMatcher<JobKey> matcher;
        if (StringUtils.isBlank(jobGroup)) {
            matcher = GroupMatcher.anyGroup();
        } else {
            matcher = GroupMatcher.groupEquals(jobGroup);
        }
        List<JobInfoEntity> list = new ArrayList<>();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            JobInfoEntity jobInfoEntity = new JobInfoEntity();
            jobInfoEntity.setJobName(jobKey.getName());
            jobInfoEntity.setJobGroup(jobKey.getGroup());
            TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            jobInfoEntity.setJobClassName(cronTrigger.getJobDataMap().getString(KEY));
            jobInfoEntity.setDescription(cronTrigger.getDescription());
            jobInfoEntity.setCronExpression(cronTrigger.getCronExpression());
            jobInfoEntity.setStartTime(DateTaskSchedulerUtils.dateToLocalDateTime(cronTrigger.getStartTime()));
            jobInfoEntity.setJobStatus(getJobStatus(triggerKey));
            JobDataMap map = scheduler.getJobDetail(jobKey).getJobDataMap();
            jobInfoEntity.setParameter(JSONUtil.toJsonStr(map));
            list.add(jobInfoEntity);
        }
        return list;
    }

    /**
     * 获取定时任务状态
     *
     * @param triggerKey 触发器
     * @return
     * @throws SchedulerException
     */
    public static TriggerStateEnum getJobStatus(TriggerKey triggerKey) throws SchedulerException {
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        return triggerState == null ? null : TriggerStateEnum.valueOf(triggerState.name());
    }
}
