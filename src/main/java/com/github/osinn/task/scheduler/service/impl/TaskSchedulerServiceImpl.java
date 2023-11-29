package com.github.osinn.task.scheduler.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.osinn.task.scheduler.entity.JobInfoEntity;
import com.github.osinn.task.scheduler.exception.TaskSchedulerException;
import com.github.osinn.task.scheduler.mapper.JobInfoMapper;
import com.github.osinn.task.scheduler.service.ITaskSchedulerService;
import com.github.osinn.task.scheduler.utils.QuartzManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务调度服务接口
 *
 * @author wency_cai
 */
@Slf4j
public class TaskSchedulerServiceImpl implements ITaskSchedulerService {

    @Resource
    private JobInfoMapper jobInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(JobInfoEntity jobInfoEntity) {
        // 校验cron是否有效
        if (!CronExpression.isValidExpression(jobInfoEntity.getCronExpression())) {
            throw new TaskSchedulerException("cron表达式错误");
        }

        if (!StringUtils.isNotBlank(jobInfoEntity.getJobName())) {
            throw new TaskSchedulerException("任务名称不能为空");
        }

        if (!StringUtils.isNotBlank(jobInfoEntity.getJobGroup())) {
            throw new TaskSchedulerException("任务组不能为空");
        }

        JobInfoEntity jobInfo = jobInfoMapper.selectOne(Wrappers.lambdaQuery(JobInfoEntity.class)
                .eq(JobInfoEntity::getJobName, jobInfoEntity.getJobName())
                .eq(JobInfoEntity::getJobGroup, jobInfoEntity.getJobGroup())
        );

        if (jobInfo != null) {
            throw new TaskSchedulerException("添加任务失败，任务组下已存在任务名称");
        }

        jobInfoMapper.insert(jobInfoEntity);

        try {
            QuartzManager.createScheduleJob(jobInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("添加任务失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(JobInfoEntity jobInfoEntity) {
        JobInfoEntity jobInfo = jobInfoMapper.selectById(jobInfoEntity.getId());
        if (jobInfo == null) {
            throw new TaskSchedulerException("任务不存在");
        }
        if (!StringUtils.isNotBlank(jobInfoEntity.getJobName())) {
            throw new TaskSchedulerException("任务名称不能为空");
        }

        if (!StringUtils.isNotBlank(jobInfoEntity.getJobGroup())) {
            throw new TaskSchedulerException("任务组不能为空");
        }
        try {
            BeanUtils.copyProperties(jobInfoEntity, jobInfo, "createTime", "createUserId");
            jobInfoMapper.updateById(jobInfo);
            QuartzManager.updateScheduleJob(jobInfoEntity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("更新任务失败");
        }
    }

    @Override
    public void pause(String jobName, String jobGroup) {
        try {
            QuartzManager.pauseScheduleJob(jobName, jobGroup);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("暂停任务失败");
        }
    }

    @Override
    public void resume(String jobName, String jobGroup) {
        try {
            QuartzManager.resumeScheduleJob(jobName, jobGroup);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("恢复任务失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String jobName, String jobGroup) {
        jobInfoMapper.delete(Wrappers.lambdaQuery(JobInfoEntity.class)
                .eq(JobInfoEntity::getJobName, jobName)
                .eq(JobInfoEntity::getJobGroup, jobGroup)
        );
        try {
            QuartzManager.deleteScheduleJob(jobName, jobGroup);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("删除任务失败");
        }
    }

    @Override
    public void runOnceTask(String jobName, String jobGroup) {
        try {
            QuartzManager.runOnce(jobName, jobGroup);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("运行一次任务失败");
        }
    }

    @Override
    public List<JobInfoEntity> findAll(String jobGroup) {
        try {
            List<JobInfoEntity> jobInfoEntityList = QuartzManager.findAll(jobGroup);
            List<String> jobNameList = new ArrayList<>();
            List<String> jobGroupList = new ArrayList<>();
            for (JobInfoEntity jobInfoEntity : jobInfoEntityList) {
                jobNameList.add(jobInfoEntity.getJobName());
                jobGroupList.add(jobInfoEntity.getJobGroup());
            }

            if (jobNameList.size() > 0) {
                List<JobInfoEntity> jobInfoList = jobInfoMapper.selectList(Wrappers.lambdaQuery(JobInfoEntity.class)
                        .in(JobInfoEntity::getJobName, jobNameList)
                        .in(JobInfoEntity::getJobGroup, jobGroupList)
                );
                Map<String, JobInfoEntity> jobInfoMap = new HashMap<>();
                for (JobInfoEntity jobInfoEntity : jobInfoList) {
                    jobInfoMap.put(jobInfoEntity.getJobName() + jobInfoEntity.getJobGroup(), jobInfoEntity);
                }

                for (JobInfoEntity jobInfoEntity : jobInfoEntityList) {
                    JobInfoEntity jobInfo = jobInfoMap.get(jobInfoEntity.getJobName() + jobInfoEntity.getJobGroup());
                    if (jobInfo != null) {
                        jobInfoEntity.setEndTime(jobInfo.getEndTime());
                    }
                }
            }
            return jobInfoEntityList;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TaskSchedulerException("查询任务失败");
        }
    }

}
