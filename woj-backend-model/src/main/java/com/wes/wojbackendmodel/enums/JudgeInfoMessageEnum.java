package com.wes.wojbackendmodel.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题信息枚举
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public enum JudgeInfoMessageEnum {

    ACCEPTED("成功", "Accepted"),
    COMPILATION_ERROR("编译错误", "Compile Error"),
    RUNTIME_ERROR("运行时错误", "Runtime Error"),
    TIME_LIMIT_EXCEEDED("超时", "Time Limit Exceeded"),
    MEMORY_LIMIT_EXCEEDED("内存超限", "Memory Limit Exceeded"),

    WAITING("等待判题", "Waiting"),
    WRONG_ANSWER("答案错误", "Wrong Answer"),
    OUTPUT_LIMIT_EXCEEDED("输出超限", "Output Limit Exceeded"),
    PRESENTATION_ERROR("输出格式错误", "Presentation Error"),
    SYSTEM_ERROR("系统错误", "System Error"),
    DANGEROUS_OPERATION("危险操作", "Dangerous Operation");


    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
