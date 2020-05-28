package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 表示的是authorizeRequest所有的security的全注解的配置实现的开端，表示的开始说明的需要的权限
     * 需要的权限分为两个部分，第一个部分是拦截的路径，第二是访问这个路径的所需要的权限。
     * antMatcheras表示拦截的所有的路径
     * anyRequest()表示的是任意的请求authenticated()认才能访问
     * .and().csrf().disable(); 固定的写法 表示的crsf的拦截失效
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
