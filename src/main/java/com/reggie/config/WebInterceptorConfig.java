package com.reggie.config;


import com.reggie.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //放行的
        List<String> pass = new ArrayList<>();
        pass.add("/login");
        pass.add("/login/**");
        pass.add("/logout");
        //拦截的
        List<String> stop = new ArrayList<>();
        stop.add("/backend/page/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns(stop).excludePathPatterns(pass);
    }
}
