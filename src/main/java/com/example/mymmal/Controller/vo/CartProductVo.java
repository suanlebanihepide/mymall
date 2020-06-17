/*
 * @Author: shenzheng
 * @Date: 2020/6/16 22:12
 */

package com.example.mymmal.Controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartProductVo implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer productId;

    private String  productName;//商品名称

    private BigDecimal price;//商品单价

    private BigDecimal total;

    private Integer quantity;

    private Integer checked;

}
