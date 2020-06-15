/*
 * @Author: shenzheng
 * @Date: 2020/6/15 23:36
 */

package com.example.mymmal.Service.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserModel implements Serializable {
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private String telphone;
    private String registerMode;
    private String thirdPartyId;
    private String encrptPassword; //用户加密密码
    private Integer role;//用户权限1为管理员
}
