package com.wes.wojbackendquestionservice.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.wojbackendcommon.common.ErrorCode;
import com.wes.wojbackendcommon.constant.CommonConstant;
import com.wes.wojbackendcommon.exception.BusinessException;
import com.wes.wojbackendcommon.utils.SqlUtils;
import com.wes.wojbackendmodel.dto.question.questionsubmit.QuestionSubmitAddRequest;
import com.wes.wojbackendmodel.dto.question.questionsubmit.QuestionSubmitQueryRequest;
import com.wes.wojbackendmodel.dto.user.vo.QuestionSubmitVO;
import com.wes.wojbackendmodel.dto.user.vo.UserVO;
import com.wes.wojbackendmodel.entity.Question;
import com.wes.wojbackendmodel.entity.QuestionSubmit;
import com.wes.wojbackendmodel.entity.User;
import com.wes.wojbackendmodel.enums.QuestionSubmitLanguageEnum;
import com.wes.wojbackendmodel.enums.QuestionSubmitStatusEnum;
import com.wes.wojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.wes.wojbackendquestionservice.rabbitMq.MyMessageProducer;
import com.wes.wojbackendserviceclient.service.JudgeFeignService;
import com.wes.wojbackendserviceclient.service.UserFeignService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author A
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2025-02-27 19:50:05
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {


    @Resource
    private QuestionService questionService;
    @Resource
    private UserFeignService userFeignService;
    @Resource
    @Lazy
    private JudgeFeignService judgeFeignService;

    @Resource
    private MyMessageProducer  myMessageProducer;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //判断编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum  questionSubmitLanguageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (questionSubmitLanguageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言不合法");
        }
        // 判断题目是否存在
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已题目提交
        long userId = loginUser.getId();
        // 每个用户串行题目提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean result = this.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
        // 题目提交数 + 1
        result = questionService.update()
                .eq("id", questionId)
                .setSql("submitNum = submitNum + 1")
                .update();
        Long  questionSubmitId = questionSubmit.getId();
        myMessageProducer.sendMessage("code_exchange","my_routingKey",String.valueOf(questionSubmitId));
//        CompletableFuture.runAsync(() -> {
//
//            judgeFeignService.doJudge(questionSubmitId);
//        });

        return questionSubmitId;
    }
    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField =  questionSubmitQueryRequest.getSortField();
        String sortOrder =  questionSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 1. 关联查询用户信息
        long userId = loginUser.getId();
        questionSubmitVO.setUserVO(userFeignService.getUserVO(loginUser));
        Question  question = questionService.getById(questionSubmit.getQuestionId());
        questionSubmitVO.setQuestionVO(questionService.getQuestionVO(question,null));
        //非本人且不为管理员
        if(userId != questionSubmit.getUserId()&&!userFeignService.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);



        return questionSubmitVOPage;
    }




}




