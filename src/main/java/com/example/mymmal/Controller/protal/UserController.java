/*
 * @Author: shenzheng
 * @Date: 2020/6/15 23:11
 */

package com.example.mymmal.Controller.protal;

import com.alibaba.druid.util.StringUtils;
import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.UserVo;
import com.example.mymmal.Service.UserService;
import com.example.mymmal.Service.model.UserModel;
import com.example.mymmal.config.error.BusinessException;
import com.example.mymmal.config.error.EmBusinessError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller
@RequestMapping("/user")
@Log
@Api(tags = "前台登录接口", description = "网站用户登录接口管理")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "前台用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "password") String password, HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.LOGIN_ERROR);
        }
        UserModel userModel = userService.login(telphone, this.EncodeByMD5(password));
        UserVo userVo = convertFromModel(userModel);
        request.getSession().setAttribute("IS_LOGIN", true);
        request.getSession().setAttribute("LOGIN_USER", userVo);
        return CommonReturnType.create(userVo);
    }



    @ApiOperation(value = "前台获取当前用户信息")
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getCurrentUser(HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;

        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        return CommonReturnType.create(userVo);
    }

    @ApiOperation(value = "用户退出")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType LogOut(HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {


        boolean is_Login = (boolean) request.getSession().getAttribute("IS_LOGIN");
        if (is_Login == false) {
            return CommonReturnType.create(null);
        }
        request.getSession().removeAttribute("IS_LOGIN");
        request.getSession().removeAttribute("LOGIN_USER");

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @ApiOperation(value = "用户注册接口")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone,
                                     @RequestParam(name = "otpCode") String otpCode,
                                     @RequestParam(name = "name") String name,
                                     @RequestParam(name = "password") String password,
                                     @RequestParam(name = "gender") String gender,
                                     @RequestParam(name = "age") Integer age,
                                     HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        //验证手机号和otpCode的一致性
//        String sessionOtpCode = (String) request.getSession().getAttribute(telphone);
//        if(StringUtils.equals(sessionOtpCode,otpCode)){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        UserModel userModel = new UserModel();
        userModel.setTelphone(telphone);
        userModel.setAge(age);
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(EncodeByMD5(password));
        try {
            userService.register(userModel);
        } catch (BusinessException e) {

            return CommonReturnType.create(e.getErrMsg(), "false");
        }
        //用户注册
        return CommonReturnType.create(null);
    }

    //用户获取otp短信接口
    @ApiOperation(value = "用户获取otp短信接口")
    @RequestMapping(value = "/getotp", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone, HttpServletRequest request) {

        //生成OTP验证码
        Random random = new Random();
        int radomInt = random.nextInt(99999);
        radomInt += 100000;
        String otpCode = String.valueOf(radomInt);
        //将otp验证码同对应用户的手机号关联,使用httpsession后边redis
        request.getSession().setAttribute(telphone, otpCode);
        //短信通知给用户
        System.out.println("telphone = " + telphone + "otp code:" + otpCode);
        return CommonReturnType.create(null);
    }


    private UserVo convertFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel, userVo);
        return userVo;
    }


    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();

        String newStr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newStr;
    }

}
