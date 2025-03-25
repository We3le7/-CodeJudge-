package com.wes.wojbackenduserservice.controller.inner;

import com.wes.wojbackendmodel.entity.User;
import com.wes.wojbackendserviceclient.service.UserFeignService;
import com.wes.wojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
@RestController
@RequestMapping("/inner")
public class userInnerController implements UserFeignService {
    @Resource
    private UserService userService;
    @Override
    @GetMapping("get/id")
    public User getById(@RequestParam("userId") long userId){
        return userService.getById(userId);

    }
    @Override
    @GetMapping("list/ids")
    public List<User> listByIds(@RequestParam("idList") Collection<Long> userIds){
        return userService.listByIds(userIds);
    }

}
