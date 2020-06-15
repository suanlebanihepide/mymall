/*
 * @Author: shenzheng
 * @Date: 2020/6/15 23:44
 */

package com.example.mymmal.config.error;

public class BusinessException extends Exception implements CommonError {


    private CommonError commonError;

    //直接接受Error的传参用于构造业务异常
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    //接收自定义errmsg方式构造异常
    public BusinessException(CommonError commonError, String errmsg) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errmsg);
    }


    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        return this.commonError.setErrMsg(errMsg);
    }


}
