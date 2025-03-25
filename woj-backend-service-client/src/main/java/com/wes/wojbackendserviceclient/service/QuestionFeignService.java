package com.wes.wojbackendserviceclient.service;

import com.wes.wojbackendmodel.entity.Question;
import com.wes.wojbackendmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author A
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-02-27 19:48:57
*/
@FeignClient(name = "woj-backend-question-service",path="/api/question/inner")
public interface QuestionFeignService
{
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") Long questionId);
    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") Long questionSubmitId);
    @PostMapping("/question_submit/update")
    Boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

}
