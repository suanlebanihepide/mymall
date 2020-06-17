/*
 * @Author: shenzheng
 * @Date: 2020/6/16 21:58
 */

package com.example.mymmal.Service.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CartModel implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer checked;

}
