/*
 * @Author: shenzheng
 * @Date: 2020/6/7 15:46
 */

package com.example.mymmal.Controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ItemVo {
    private Integer id;
    //商品名称
    private String title;
    //商品价格
    private BigDecimal price;
    //商品库存
    private Integer stock;
    //商品描述
    private String description;
    //销量
    private Integer sales;
    //图片url
    private String imgUrl;

    //记录商品是否在秒杀活动中  ，0表示没有秒杀，1秒杀待开始，2秒杀进行
    private Integer promoStatus;
    //秒杀价格
    private BigDecimal promoPrice;

    //秒杀活动ID
    private Integer promoId;

    //秒杀开始时间
    private String startDate;

}
