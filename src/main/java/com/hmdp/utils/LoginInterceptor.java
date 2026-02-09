package com.hmdp.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    //不能通过注解注入，因为拦截器在MvcConfig是通过new手动创建的
    //如果在本类添加@Component注解即可通过注解注入
    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //第二层拦截器，根据ThreadLocal是否有值来判断需不需要拦截
        if (UserHolder.getUser() == null) {
            response.setStatus(401);
            return false;
        }
        //有用户则放行
        return true;
    }
}
