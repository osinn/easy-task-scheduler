package com.github.osinn.task.scheduler.config;

import com.github.osinn.task.scheduler.utils.QuartzManager;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;


/**
 * @author wency_cai
 */
public class EasyTaskSchedulerStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(EasyTaskSchedulerStartListener.class);

    public EasyTaskSchedulerStartListener() {

    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("任务调度初始化");
        Scheduler scheduler = applicationContext.getBean(Scheduler.class);
        QuartzManager.setScheduler(scheduler);
    }
}