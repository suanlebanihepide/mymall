/*
 * @Author: shenzheng
 * @Date: 2020/6/16 23:24
 */

package com.example.mymmal.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyInterceptorConfig implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            //统一拦截（查询当前session是否存在user）(这里user会在每次登陆成功后，写入session)
            Object flag=request.getSession().getAttribute("IS_LOGIN");
            if(flag!=null){
                return true;
            }
            response.sendRedirect(request.getContextPath()+"/login");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;//如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
