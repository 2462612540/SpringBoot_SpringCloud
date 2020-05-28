package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("tensquare-user")
public interface UserClient {
    /**
     * 更新好友粉丝数和用户关注数
     * @param userid
     * @param friendid
     * @param x
     */
    @RequestMapping(value = "/user/{userid}/{friendid}/{x}",method= RequestMethod.PUT)
    public void updatefanscountandfollowcount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("x") Integer x);
}
