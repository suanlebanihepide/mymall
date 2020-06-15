/*
 * @Author: shenzheng
 * @Date: 2020/6/16 1:14
 */

package com.example.mymmal.config.error;

import com.example.mymmal.Controller.response.CommonReturnType;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Log
public class ExceptionResolve {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonReturnType resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        log.info("{" + httpServletRequest.getRequestURI() + "} Exception" + e);
        Map<String, String> map = new HashMap<>();
        map.put("status", "false");
        map.put("msg", "详情查看服务端接口信息");
        map.put("data", e.toString());
        return CommonReturnType.create(map, "false");
    }
}
