package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 在请求前pre或者请求后post执行
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器执行的顺序，数字越小，表示越先执行
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启true表示开启
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作 return任何object的值都表示继续执行
     * setsendzuulResponse(false)表示不再继续执行
     *
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = requestContext.getRequest();

        //zuul第一次请求放行
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }
        //登陆请求放行
        if (request.getRequestURI().indexOf("login") > 0) {
            return null;
        }
        //得到头信息
        String header = request.getHeader("Authorization");
        //判断是否有头信息
        if (StringUtils.isNotBlank(header)) {
            if (header.startsWith("Bearer ")) {
                String token = header.substring(7);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if (roles.equals("admin")) {
                        //把头信息转发下去，并且放行
                        requestContext.addZuulResponseHeader("Authorization", header);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //终止运行
                    requestContext.setSendZuulResponse(false);
                }
            }
        }
        //终止运行
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody("权限不足");
        requestContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
