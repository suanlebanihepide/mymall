/*
 * @Author: shenzheng
 * @Date: 2020/6/15 23:42
 */

package com.example.mymmal.config.error;

import io.swagger.annotations.Api;

@Api(tags = "错误信息枚举")
public enum EmBusinessError implements CommonError {
    //定义错误类型
    PARAMETER_VALIDATION_ERROR(000001, "参数不合法"),
    UNKNOW_ERROR(000002, "参数不合法"),
    TELPHONE_ERROR(000003, "手机号码重复"),
    NOT_LOGIN(000005, "用户未登录"),
    LOGIN_ERROR(000004, "用户手机号或者密码错误"),
    ;
    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {

        this.errMsg = errMsg;
        return this;
    }
}
