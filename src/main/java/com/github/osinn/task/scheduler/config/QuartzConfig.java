package com.github.osinn.task.scheduler.config;

import com.github.osinn.task.scheduler.service.ITaskSchedulerService;
import com.github.osinn.task.scheduler.service.impl.TaskSchedulerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述
 *
 * @author wency_cai
 */
@Slf4j
@MapperScan("com.github.osinn.task.scheduler.mapper")
@Configuration
public class QuartzConfig {

    @Bean
    @ConditionalOnMissingBean(ITaskSchedulerService.class)
    public ITaskSchedulerService taskSchedulerService() {
        return new TaskSchedulerServiceImpl();
    }

    @Bean
    public EasyTaskSchedulerStartListener easyTaskSchedulerStartListener() {
        return new EasyTaskSchedulerStartListener();
    }

}