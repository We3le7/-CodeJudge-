package com.wes.wojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wes.wojbackendmodel.dto.question.questionsubmit.QuestionSubmitAddRequest;
import com.wes.wojbackendmodel.dto.question.questionsubmit.QuestionSubmitQueryRequest;
import com.wes.wojbackendmodel.dto.user.vo.QuestionSubmitVO;
import com.wes.wojbackendmodel.entity.QuestionSubmit;
import com.wes.wojbackendmodel.entity.User;

/**
* @author A
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-02-27 19:50:05
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
   Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);
    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,User loginUser);


}
