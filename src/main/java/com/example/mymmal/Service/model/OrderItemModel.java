/*
 * @Author: shenzheng
 * @Date: 2020/6/18 0:53
 */

package com.example.mymmal.Service.model;

import com.example.mymmal.dataobject.ShippingDO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItemModel implements Serializable {

    private String id;

    private Integer userId;
    //购买商品ID
    private Integer itemId;
    //购买商品的单价
    private BigDecimal itemPrice;
    //购买数量
    private Integer amount;
    //购买金额
    private BigDecimal orderAmount;

    private Integer promoId;
    private Integer status;//订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭

    private String send_time;//发货时间

    private Integer shippingId;

}
