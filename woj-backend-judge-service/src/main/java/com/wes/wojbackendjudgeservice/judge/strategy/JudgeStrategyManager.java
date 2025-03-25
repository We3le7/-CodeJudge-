package com.wes.wojbackendjudgeservice.judge.strategy;


import com.wes.wojbackendmodel.codesandbox.JudgeInfo;

public class JudgeStrategyManager {
    public JudgeInfo doJudge(JudgeContext context) {
        String language = context.getQuestionSubmit().getLanguage();

       JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
       if (language.equals("java")) {
            return new JavaJudgeStrategy().doJudge(context);
        }
        return judgeStrategy.doJudge(context);
    }
}
