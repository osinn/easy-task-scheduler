package com.github.osinn.task.scheduler.dto;

import com.github.osinn.task.scheduler.enums.TimeModes;
import lombok.Data;

/**
 * 描述
 *
 * @author wency_cai
 */
@Data
public class PartitionDTO {

    /**
     * 分钟
     */
    private Long minute;

    /**
     * 月
     */
    private Long month;

    /**
     * 时
     */
    private Long hour;

    /**
     * 星期几
     */
    private Long weekDay;

    /**
     * 日期单位
     */
    private TimeModes periodUnit;

    /**
     * 天数
     */
    private Long day;

    public void setMinute(Long minute) {
        this.minute = minute == null ? 0 : minute;
    }

    public void setMonth(Long month) {
        this.month = month == null ? 0 : month;
    }

    public void setHour(Long hour) {
        this.hour = hour == null ? 0 : hour;
    }

    public void setWeekDay(Long weekDay) {
        this.weekDay = weekDay == null ? 0 : weekDay;
    }

    public void setDay(Long day) {
        this.day = day == null ? 0 : day;
    }
}
