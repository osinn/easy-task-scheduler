package com.github.osinn.task.scheduler.service;

import com.github.osinn.task.scheduler.entity.JobInfoEntity;

import java.util.List;

/**
 * 任务调度服务接口
 *
 * @author wency_cai
 */
public interface ITaskSchedulerService {

    /**
     * 查询任务组所有任务信息
     *
     * @param jobGroup 任务组
     */
    List<JobInfoEntity> findAll(String jobGroup);

    /**
     * 添加任务
     *
     * @param jobInfoEntity 任务信息
     */
    void save(JobInfoEntity jobInfoEntity);

    /**
     * 更新任务
     */
    void update(JobInfoEntity jobInfoEntity);

    /**
     * 暂停任务
     */
    void pause(String jobName, String jobGroup);

    /**
     * 恢复任务
     */
    void resume(String jobName, String jobGroup);

    /**
     * 删除任务
     */
    void delete(String jobName, String jobGroup);

    /**
     * 删除任务
     */
    void runOnceTask(String jobName, String jobGroup);

}
