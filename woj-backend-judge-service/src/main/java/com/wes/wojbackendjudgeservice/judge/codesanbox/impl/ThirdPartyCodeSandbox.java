package com.wes.wojbackendjudgeservice.judge.codesanbox.impl;


import com.wes.wojbackendjudgeservice.judge.codesanbox.CodeSandbox;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * 预留对接第三方代码沙箱
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ThirdPartyCodeSandBox");
        return null;
    }
}
