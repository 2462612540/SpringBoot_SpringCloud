package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import entity.Result;
import entity.StatusCode;
import com.tensquare.friend.service.FriendService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/com/tensquare/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private UserClient userClient;

    @RequestMapping(value = "/like/{friendId}/{type}", method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendId, @PathVariable String type) {
        //验证是否登录，拿到当前登录的用户id
        Claims claims = (Claims) request.getAttribute("claims_user");
        if (claims == null) {
            //说明当前用户没有user角色
            return new Result(false, StatusCode.LOGINERROR, "无权访问");
        }
        //得到当前用户id
        String userId = claims.getId();
        //判断是添加好友还是添加非好友
        if (StringUtils.isNotBlank(type)) {
            if (type.equals("1")) {
                //添加好友
                int flag = friendService.addFriend(userId, friendId);
                if (flag == 0) {
                    return new Result(false, StatusCode.ERROR, "不能重复添加好友");
                } else if (flag == 1) {
                    userClient.updatefanscountandfollowcount(userId, friendId, 1);
                    return new Result(true, StatusCode.OK, "添加成功");
                }
            } else if (type.equals("2")) {
                //添加非好友
                int flag = friendService.addNoFriend(userId, friendId);
                if (flag == 0) {
                    return new Result(false, StatusCode.ERROR, "不能重复添加非好友");
                } else if (flag == 1) {
                    return new Result(true, StatusCode.OK, "添加成功");
                }
            }
            return new Result(false, StatusCode.ERROR, "参数异常");
        } else {
            return new Result(false, StatusCode.ERROR, "参数异常");
        }

    }

    @RequestMapping(value = "/{friendId}", method = RequestMethod.DELETE)
    public Result addFriend(@PathVariable String friendId) {
        //验证是否登录，拿到当前登录的用户id
        Claims claims = (Claims) request.getAttribute("claims_user");
        if (claims == null) {
            //说明当前用户没有user角色
            return new Result(false, StatusCode.LOGINERROR, "无权访问");
        }
        //得到当前用户id
        String userId = claims.getId();
        friendService.deleteFriend(userId, friendId);
        userClient.updatefanscountandfollowcount(userId, friendId, -1);
        return new Result(false, StatusCode.OK, "删除成功");
    }

}
