/**
 * Copyright (C), 2018-2020
 * FileName: BaseClient
 * Author:   xjl
 * Date:     2020/5/28 19:48
 * Description: 其他微服务的调用
 */
package com.tensquare.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tensquare-base")
public interface BaseClient {

    /**
     * 根据ID查询
     *
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/label/{labelId}", method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId);
}
