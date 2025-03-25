package com.wes.wojbackendjudgeservice.judge.strategy;

import com.wes.wojbackendmodel.codesandbox.JudgeInfo;
import com.wes.wojbackendmodel.dto.question.JudgeCase;
import com.wes.wojbackendmodel.entity.Question;
import com.wes.wojbackendmodel.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private List<JudgeCase>  judgeCaseList;
    private Question question;
    private QuestionSubmit questionSubmit;

}
