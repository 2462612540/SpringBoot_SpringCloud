package com.tensquare.qa.client;

import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

@Component
public class BaseClientImp implements BaseClient {
    @Override
    public Result findById(String labelId) {
        return new Result(false, StatusCode.ERROR, "熔断器启动了");
    }
}
