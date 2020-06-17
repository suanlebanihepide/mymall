package com.example.mymmal.Service;

import com.example.mymmal.Service.model.CartModel;

import java.util.List;

public interface CartService {

    //添加商品
    List<CartModel> add(Integer userId, Integer productId, Integer count);

    //获取购物车列表
    List<CartModel> getListByUserId(Integer userId);

    //更新购物车
    List<CartModel> update(Integer userId,Integer productId,Integer count);



    //获取购物车列表
    List<CartModel> deleteProduct(Integer userId,String productIds);


    //选中或者反选
    List<CartModel> selectOrUnSelect (Integer userId,Integer productId,Integer checked);



}
