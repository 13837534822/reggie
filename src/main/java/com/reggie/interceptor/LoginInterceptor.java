package com.reggie.interceptor;

import com.reggie.common.BaseContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        System.out.println("拦截请求：" + url);
        if (url.contains("login") || url.contains("logout")) {
            System.out.println("登录/登出放行");
            return true;
        }
        if (request.getSession().getAttribute("employee") != null) {
            System.out.println("已登录，放行");
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee")); //将登陆者的ID放入多线程中
            return true;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
