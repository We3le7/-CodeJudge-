package com.wes.wojbackendjudgeservice.judge;


import com.wes.wojbackendmodel.entity.QuestionSubmit;

public interface JudgeService {
    /**
     * 判题服务
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
