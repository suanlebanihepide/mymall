/*
 * @Author: shenzheng
 * @Date: 2020/6/16 22:11
 */

package com.example.mymmal.Controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartVo implements Serializable {


    private List<CartProductVo> cartProductVoList;

    private BigDecimal cartTotalPrice;

    private Boolean allChecked;//是否已经都勾选
}
