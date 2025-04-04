package com.wes.wojbackendjudgeservice.judge.codesanbox;


import com.wes.wojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
