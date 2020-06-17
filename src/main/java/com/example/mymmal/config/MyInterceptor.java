/*
 * @Author: shenzheng
 * @Date: 2020/6/16 23:22
 */

package com.example.mymmal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptor implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new MyInterceptorConfig());
        registration.addPathPatterns("/**");                      //所有路径都被拦截
        registration.excludePathPatterns(                         //添加不拦截路径
                "/user/login",            //登录
                "/manage_user/login", //后台用户登录
                "/**/*.html",            //html静态资源
                "/**/*.js",              //js静态资源
                "/**/*.css",             //css静态资源
                "/**/*.ttf"
        );
    }
}

