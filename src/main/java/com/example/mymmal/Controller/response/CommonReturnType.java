/*
 * @Author: shenzheng
 * @Date: 2020/6/16 0:06
 */

package com.example.mymmal.Controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
public class CommonReturnType implements Serializable {


    //固定返回信息
    private String status;
    private Object data;

    public CommonReturnType() {

    }

    public CommonReturnType(String status, Object result) {
        this.status = status;
        this.data = result;
    }

    //定义通用创建方法
    public static CommonReturnType create(Object result) {

        return new CommonReturnType("success", result);
    }

    public static CommonReturnType create(Object result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

}
