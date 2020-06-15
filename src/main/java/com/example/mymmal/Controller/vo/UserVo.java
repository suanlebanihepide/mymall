/*
 * @Author: shenzheng
 * @Date: 2020/6/6 22:54
 */

package com.example.mymmal.Controller.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVo implements Serializable {
    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;
    private String telphone;
    private Integer role;//用户权限1为管理员
}
