package com.wes.wojbackendjudgeservice.judge.codesanbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wes.wojbackendcommon.common.ErrorCode;
import com.wes.wojbackendcommon.exception.BusinessException;
import com.wes.wojbackendjudgeservice.judge.codesanbox.CodeSandbox;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeRequest;
import com.wes.wojbackendmodel.codesandbox.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 本项目实现方式
 */
public class RemoteCodeSandbox implements CodeSandbox {
    private static final String AUTH_REQUEST_HEADER="auth";
    private static final String AUTH_REQUEST_SECRET="secretKey";
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("RemoteCodeSandBox");
        String url="http://localhost:8090/executeCode";
        String json= JSONUtil.toJsonStr(executeCodeRequest);
        String response= HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if(StringUtils.isBlank(response)){
            throw new BusinessException( ErrorCode.API_REQUEST_ERROR,"远程代码沙箱执行错误"+response);

        }
        return JSONUtil.toBean(response,ExecuteCodeResponse.class);
    }

}
