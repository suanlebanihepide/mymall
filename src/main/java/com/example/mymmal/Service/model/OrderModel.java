/*
 * @Author: shenzheng
 * @Date: 2020/6/18 0:59
 */

package com.example.mymmal.Service.model;

import com.example.mymmal.dataobject.ShippingDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderModel {
    //订单ID
    private String id;
    //购买用户ID
    private Integer userId;
    List<OrderItemModel> OrderProductList;

    BigDecimal total;//订单总价

    private Integer status;//订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭

    private String send_time;//发货时间

    private ShippingDO shippingDO;
}
