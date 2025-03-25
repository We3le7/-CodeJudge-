package com.wes.wojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;

import com.wes.wojbackendcommon.common.ErrorCode;
import com.wes.wojbackendcommon.exception.BusinessException;
import com.wes.wojbackendjudgeservice.judge.codesanbox.CodeSandbox;
import com.wes.wojbackendjudgeservice.judge.codesanbox.CodeSandboxFactory;
import com.wes.wojbackendjudgeservice.judge.codesanbox.CodeSandboxProxy;
import com.wes.wojbackendjudgeservice.judge.strategy.JudgeContext;
import com.wes.wojbackendjudgeservice.judge.strategy.JudgeStrategyManager;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeResponse;
import com.wes.wojbackendmodel.codesandbox.JudgeInfo;
import com.wes.wojbackendmodel.dto.question.JudgeCase;
import com.wes.wojbackendmodel.entity.Question;
import com.wes.wojbackendmodel.entity.QuestionSubmit;
import com.wes.wojbackendmodel.enums.QuestionSubmitStatusEnum;
import com.wes.wojbackendserviceclient.service.QuestionFeignService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionFeignService questionFeignService;
    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionFeignService.getQuestionSubmitById(questionSubmitId);
        if(questionSubmit == null){
           throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignService.getQuestionById(questionId);
        if(question == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //更改判题状态，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignService.updateQuestionSubmitById(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        //如果不为等待状态
        if(!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"判题中，请勿连续提交");
        }
        //调用代码沙箱
        CodeSandbox codeSandbox = CodeSandboxFactory.createCodeSandbox(type);
        codeSandbox =new CodeSandboxProxy(codeSandbox);
        String judgeCaseStr = question.getJudgeCase();
        //获取判题需要的参数
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr,JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        //执行代码
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        JudgeInfo judgeResult=new JudgeInfo();
        //修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        if(executeCodeResponse.getStatus()==QuestionSubmitStatusEnum.FAILED.getValue()){
            judgeResult.setMessage(executeCodeResponse.getMessage());
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        }else{

            List<String> outputList = executeCodeResponse.getOutputList();
            JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
            JudgeContext judgeContext =new JudgeContext();
            judgeContext.setInputList( inputList);
            judgeContext.setJudgeCaseList(judgeCaseList);
            judgeContext.setOutputList(outputList);
            judgeContext.setQuestion(question);
            judgeContext.setJudgeInfo(judgeInfo);
            judgeContext.setQuestionSubmit(questionSubmit);
            JudgeStrategyManager judgeStrategyManager = new JudgeStrategyManager();
            //使用策略管理根据语言进行判题策略的特异性处理
            judgeResult = judgeStrategyManager.doJudge(judgeContext);
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        }


        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeResult));
        update = questionFeignService.updateQuestionSubmitById(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        QuestionSubmit  questionSubmitResult = questionFeignService.getQuestionSubmitById(questionSubmitId);
        return  questionSubmitResult;
    }
}
