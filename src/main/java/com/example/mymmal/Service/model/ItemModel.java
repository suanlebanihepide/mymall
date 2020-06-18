/*
 * @Author: shenzheng
 * @Date: 2020/6/16 3:19
 */

package com.example.mymmal.Service.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ItemModel implements Serializable {
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

    //商品类别
    private Integer category_id;

    //商品当前状态
    private Integer status;

    //使用聚合模型,如果不为空则表示有还未结束的秒杀活动
    private PromoModel promoModel;
}
