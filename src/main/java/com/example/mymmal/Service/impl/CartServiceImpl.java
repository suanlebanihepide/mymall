/*
 * @Author: shenzheng
 * @Date: 2020/6/16 21:49
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.CartService;
import com.example.mymmal.Service.model.CartModel;
import com.example.mymmal.Service.model.ItemModel;
import com.example.mymmal.dao.CartDOMapper;
import com.example.mymmal.dao.ItemDOMapper;
import com.example.mymmal.dataobject.CartDO;
import com.example.mymmal.dataobject.ItemDO;
import com.example.mymmal.dataobject.ItemStockDO;
import com.google.common.base.Splitter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDOMapper cartDOMapper;
    @Autowired
    private ItemDOMapper itemDOMapper;

    @Override
    public List<CartModel> add(Integer userId, Integer productId, Integer count) {

        if (productId == null || count == null)
            return null;
        CartDO cartDO = cartDOMapper.selectCartByUserIdProductId(userId, productId);
        if (cartDO == null) {
            CartDO cartItem = new CartDO();
            cartItem.setQuantity(count);
            cartItem.setChecked(1);//商品是否被勾选
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartDOMapper.insertSelective(cartItem);
        } else {
            //商品已经在购物车内了，更新商品数量
            count = cartDO.getQuantity() + count;
            cartDO.setQuantity(count);
            cartDOMapper.updateByPrimaryKeySelective(cartDO);
        }
        return getListByUserId(userId);
    }

    @Override
    public List<CartModel> getListByUserId(Integer userId) {
        List<CartDO> list = cartDOMapper.selectCartByUserId(userId);

        List<CartModel> cartModels = list.stream().map(cartDO -> {
            return convertFromCartDoToCartModel(cartDO);
        }).collect(Collectors.toList());
        return cartModels;
    }

    @Override
    public List<CartModel> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return null;
        }
        CartDO cart = cartDOMapper.selectCartByUserIdProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartDOMapper.updateByPrimaryKey(cart);

        return getListByUserId(userId);
    }

    @Override
    public List<CartModel> deleteProduct(Integer userId, String productIds) {
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)) {
            return null;
        }
        cartDOMapper.deleteByUserIdProductIds(userId, productList);
        return getListByUserId(userId);
    }

    @Override
    public List<CartModel> selectOrUnSelect(Integer userId, Integer productId, Integer checked) {

        cartDOMapper.checkedOrUncheckedProduct(userId, productId, checked);
        return getListByUserId(userId);
    }


    private CartModel convertFromCartDoToCartModel(CartDO cartDO) {
        CartModel cartModel = new CartModel();
        BeanUtils.copyProperties(cartDO, cartModel);
        return cartModel;
    }
}
