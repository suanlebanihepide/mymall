/*
 * @Author: shenzheng
 * @Date: 2020/6/16 3:20
 */

package com.example.mymmal.Service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PromoModel {
    private Integer id;

    //秒杀活动状态1表示未开始，2表示进行中，3表示已结束
    private Integer status;

    //秒杀活动名称
    private String promoName;

    //开始时间
    private Date startDate;
    private Date endDate;
    //秒杀活动的使用商品
    private Integer itemId;
    //秒杀价格
    private BigDecimal promoItemPrice;

}
