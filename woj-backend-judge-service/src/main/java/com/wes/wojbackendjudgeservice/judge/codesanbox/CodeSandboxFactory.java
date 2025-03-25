package com.wes.wojbackendjudgeservice.judge.codesanbox;


import com.wes.wojbackendjudgeservice.judge.codesanbox.impl.ExampleCodeSandbox;
import com.wes.wojbackendjudgeservice.judge.codesanbox.impl.RemoteCodeSandbox;
import com.wes.wojbackendjudgeservice.judge.codesanbox.impl.ThirdPartyCodeSandbox;

public class CodeSandboxFactory {
    /**
     * 创建代码沙箱示例

     * @param type
     * @return
     */
    public static CodeSandbox createCodeSandbox(String type) {
        // TODO 如果确定沙箱安全那么可以实现单例工厂模式
        switch (type){
            case "remote":
                return new RemoteCodeSandbox();
            case"thirdParty":
                return new ThirdPartyCodeSandbox();
            case "example":
            default:
                return new ExampleCodeSandbox();

   }
    }
}
