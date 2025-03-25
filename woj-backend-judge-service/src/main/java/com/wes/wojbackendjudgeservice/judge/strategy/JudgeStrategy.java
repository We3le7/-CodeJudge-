package com.wes.wojbackendjudgeservice.judge.strategy;


import com.wes.wojbackendmodel.codesandbox.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);

}
