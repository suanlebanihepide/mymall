/*
 * @Author: shenzheng
 * @Date: 2020/6/16 0:06
 */

package com.example.mymmal.Controller.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CommonReturnType<T> implements Serializable {


    //固定返回信息
    @ApiModelProperty(value = "状态", name = "status")
    private String status;
    @ApiModelProperty(value = "返回的数据", name = "data")
    private Object data;

    public CommonReturnType() {

    }

    public CommonReturnType(String status, Object result) {
        this.status = status;
        this.data = result;
    }

    //定义通用创建方法
    public static <T> CommonReturnType<T> create(T result) {

        return new CommonReturnType("success", result);
    }

    public static <T> CommonReturnType<T> create(T result, String status) {
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }

}
