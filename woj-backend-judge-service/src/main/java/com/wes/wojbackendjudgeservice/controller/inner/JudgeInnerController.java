package com.wes.wojbackendjudgeservice.controller.inner;

import com.wes.wojbackendjudgeservice.judge.JudgeService;
import com.wes.wojbackendmodel.entity.QuestionSubmit;
import com.wes.wojbackendserviceclient.service.JudgeFeignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignService {
    @Resource
    private JudgeService judgeService;
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(Long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
