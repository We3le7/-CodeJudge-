package com.wes.wojbackendserviceclient.service;


import com.wes.wojbackendmodel.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "woj-backend-judge-service", path = "/api/judge/inner")
public interface JudgeFeignService {
    @PostMapping("/do")
    QuestionSubmit doJudge( @RequestParam Long questionSubmitId);
}
