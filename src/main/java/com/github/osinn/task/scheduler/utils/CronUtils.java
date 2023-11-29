package com.github.osinn.task.scheduler.utils;

import com.github.osinn.task.scheduler.dto.PartitionDTO;
import com.github.osinn.task.scheduler.enums.TimeModes;

/**
 * 描述
 *
 * @author wency_cai
 */
public class CronUtils {

    public static String getCronExpressionByPartition(PartitionDTO partitionDTO) {
        TimeModes periodUnit = partitionDTO.getPeriodUnit();
        Long minute = partitionDTO.getMinute();
        Long hour = partitionDTO.getHour();
        Long day = partitionDTO.getDay();
        Long month = partitionDTO.getMonth();
        Long weekDay = partitionDTO.getWeekDay();
        switch (periodUnit) {
            case MINUTE:
                minute = minute > 59 ? 59 : minute;
                return "0 */" + minute + " * * * ?";
            case HOUR:
                return "0 " + minute + " * * * ?";
            case DAY:
                return "0 " + minute + " " + hour + " * * ?";
            case WEEK:
                return "0 " + minute + " " + hour + " ? * " + weekDay;
            case MONTH:
                return "0 " + minute + " " + hour + " " + day + " * ?";
            case YEAR:
                return "0 " + minute + " " + hour + " " + day + " " + month + " ?";
            default:
                return "0 */10 * * * ?";
        }
    }
}
