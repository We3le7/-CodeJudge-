package com.wes.wojbackendjudgeservice.judge.codesanbox.impl;


import com.wes.wojbackendjudgeservice.judge.codesanbox.CodeSandbox;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeResponse;
import com.wes.wojbackendmodel.codesandbox.JudgeInfo;
import com.wes.wojbackendmodel.enums.JudgeInfoMessageEnum;
import com.wes.wojbackendmodel.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 只为跑通流程
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList=executeCodeRequest.getInputList();
        JudgeInfo judgeInfo=new  JudgeInfo();
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        ExecuteCodeResponse executeCodeResponse = ExecuteCodeResponse.builder()
                .message("测试执行成功")
                .status(QuestionSubmitStatusEnum.SUCCEED.getValue())
                .judgeInfo( judgeInfo)
                .outputList(inputList)
                .build();

        return executeCodeResponse;
    }

}
