package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    /**
     * 在请求前pre或者请求后post执行
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 多个过滤器执行的顺序，数字越小，表示越先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 当前过滤器是否开启true表示开启
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作 return任何object的值都表示继续执行
     * setsendzuulResponse(false)表示不再继续执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        RequestContext currentContext = RequestContext.getCurrentContext();
        //得到request域
        HttpServletRequest request = currentContext.getRequest();
        //得到头信息
        String header = request.getHeader("Authorization");
        //判断是否有头信息
        if (StringUtils.isNotBlank(header)){
            //把头信息继续向下传
            currentContext.addZuulResponseHeader("Authorization", header);
        }
        return null;
    }
}
