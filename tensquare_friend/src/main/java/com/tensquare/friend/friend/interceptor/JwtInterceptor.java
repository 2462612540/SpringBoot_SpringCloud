package com.tensquare.friend.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //无论如何都放行，具体能不能操作还是在具体的操作中判断
        //拦截器只是负责把有请求头中包含token的令牌进行一个解析验证
        String header = request.getHeader("Authorization");

        if (StringUtils.isNotBlank(header)){
            //如果有包含Authorization头信息，就对其进行解析
            if (header.startsWith("Bearer ")){
                //得到token
                String token = header.substring(7);
                //对令牌进行验证
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (!StringUtils.isEmpty(roles) && roles.equals("user")){
                        //如果是用户
                        request.setAttribute("claims_user", claims);
                    }
                }catch (Exception e){
                    throw new RuntimeException("令牌不正确");
                }
            }
        }
        return true;
    }
}
