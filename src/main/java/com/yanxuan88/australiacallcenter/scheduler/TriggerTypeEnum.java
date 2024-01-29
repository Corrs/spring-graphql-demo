package com.yanxuan88.australiacallcenter.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanxuan88.australiacallcenter.common.IDict;
import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.ScheduleBuilder;

import java.util.Map;

/**
 * Quartz trigger type
 *
 * @author caosh
 * @since 2024-01-26 21:00:00
 */
public enum TriggerTypeEnum implements IDict<Integer> {
    CRON(0, "cron") {
        @Override
        protected boolean checkTriggerRule(String triggerRule) {
            String expression = (String) (ruleMap(triggerRule).get(getText()));
            return expression == null || CronExpression.isValidExpression(expression);
        }

        @Override
        public ScheduleBuilder scheduleBuilder(String triggerRule) {
            if (checkTriggerRule(triggerRule)) {
                return CronScheduleBuilder.cronSchedule((String) (ruleMap(triggerRule).get(getText())));
            }
            throw new RuntimeException("cron表达式错误");
        }
    },
    SIMPLE(1, "simple") {
        @Override
        protected boolean checkTriggerRule(String triggerRule) {
            return false;
        }

        @Override
        public ScheduleBuilder scheduleBuilder(String triggerRule) {
            return null;
        }
    },
    CALENDAR(2, "calendar") {
        @Override
        protected boolean checkTriggerRule(String triggerRule) {
            return false;
        }

        @Override
        public ScheduleBuilder scheduleBuilder(String triggerRule) {
            return null;
        }
    },
    DAILY_TIME(3, "dailytime") {
        @Override
        protected boolean checkTriggerRule(String triggerRule) {
            return false;
        }

        @Override
        public ScheduleBuilder scheduleBuilder(String triggerRule) {
            return null;
        }
    };

    TriggerTypeEnum(int code, String text) {
        init(code, text);
    }

    static final ObjectMapper MAPPER = new ObjectMapper();

    Map<String, Object> ruleMap(String rule) {
        try {
            return MAPPER.readValue(rule, MAPPER.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("触发器规则解析失败");
        }
    }

    static ScheduleBuilder scheduleBuilder(Integer triggerType, String triggerRule) {
        return type(triggerType).scheduleBuilder(triggerRule);
    }

    static boolean checkTriggerRule(Integer triggerType, String triggerRule) {
        return type(triggerType).checkTriggerRule(triggerRule);
    }

    protected abstract boolean checkTriggerRule(String triggerRule);

    private static TriggerTypeEnum type(Integer triggerType) {
        TriggerTypeEnum type = IDict.getByCode(TriggerTypeEnum.class, triggerType);
        if (type == null) throw new RuntimeException("无效的触发器类型");
        return type;
    }

    protected abstract ScheduleBuilder scheduleBuilder(String triggerRule);
}
