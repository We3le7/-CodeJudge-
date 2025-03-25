package com.wes.wojbackendmodel.dto.question.questionsubmit;


import com.wes.wojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;


    /**
     * 提交状态
     */
    private Integer status;


    /**
     * 題目id
     */
    private Long questionId;
    /**
     * Userid
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}