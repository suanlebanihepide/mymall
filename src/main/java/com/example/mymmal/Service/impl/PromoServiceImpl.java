/*
 * @Author: shenzheng
 * @Date: 2020/6/18 1:20
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.ItemService;
import com.example.mymmal.Service.PromoService;
import com.example.mymmal.Service.UserService;
import com.example.mymmal.Service.model.ItemModel;
import com.example.mymmal.Service.model.PromoModel;
import com.example.mymmal.Service.model.UserModel;
import com.example.mymmal.dao.PromoDOMapper;
import com.example.mymmal.dataobject.PromoDO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PromoServiceImpl implements PromoService {
    @Autowired
    private PromoDOMapper promoDOMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;


    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);

        PromoModel promoModel = convertFromDataObject(promoDO);
        if (promoModel == null) return null;
        Date now = new Date();
        if (now.before(promoModel.getStartDate())) {
            promoModel.setStatus(1);
        } else if (now.after(promoModel.getEndDate())) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    @Override
    @Transactional
    public Boolean submitPromo(String promoName, Map<Integer, BigDecimal> map, Date startTime, Date endTime) {
        for (Map.Entry<Integer, BigDecimal> entry : map.entrySet()) {
            Integer key = entry.getKey();
            BigDecimal vaule = entry.getValue();
            PromoDO promoDO = new PromoDO();
            promoDO.setItemId(key);
            promoDO.setPromoName(promoName);
            promoDO.setPromoItemPrice(vaule.doubleValue());
            promoDO.setStartDate(startTime);
            promoDO.setEndDate(endTime);
            promoDOMapper.insertSelective(promoDO);
        }
        return true;
    }

    @Override
    public void publishPromo(Integer promoId) {

        //通过id获取活动
        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);
        if (promoDO == null || promoDO.getItemId() == 0) {
            return;
        }
        //拿到商品库存
        ItemModel itemModel = itemService.getItemModelById(promoDO.getItemId());
        //同步库存到redis
        redisTemplate.opsForValue().set("promo_item_stock" + itemModel.getId(), itemModel.getStock());
        //将大闸限制数字设置到redis中，限制令牌发放数量
        redisTemplate.opsForValue().set("promo_door_count_" + promoId, itemModel.getStock() * 5);
    }

    @Override
    public String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId) {
        if (redisTemplate.hasKey("promo_item_stock_validate" + itemId)) {
            return null;
        }

        PromoDO promoDO = promoDOMapper.selectByPrimaryKey(promoId);

        PromoModel promoModel = convertFromDataObject(promoDO);
        if (promoModel == null) return null;

        Date now = new Date();
        if (now.before(promoModel.getStartDate())) {
            promoModel.setStatus(1);
        } else if (now.after(promoModel.getEndDate())) {
            promoModel.setStatus(3);
        } else {
            promoModel.setStatus(2);
        }

        if (promoModel.getStatus() != 2) {
            return null;
        }

        //上边判断活动信息，判断Item和user信息
        ItemModel itemModel = itemService.getItemModelById(itemId);
        if (itemModel == null || itemModel.getStatus() != 1) {
            return null;
        }
//        UserModel userModel = userService.getUserById(userId);
//        if (userModel == null) {
//            return null;
//        }
        //获取秒杀大闸count数量
        long result = redisTemplate.opsForValue().increment("promo_door_count_" + promoId, -1);
        if (result < 0) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("promo_token" + promoId + "user_id" + userId + "itemId" + itemId, token);
        redisTemplate.expire("promo_token" + promoId + "user_id" + userId + "itemId" + itemId, 5, TimeUnit.MINUTES);
        return token;
    }

    private PromoModel convertFromDataObject(PromoDO promoDO) {
        if (promoDO == null) return null;
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice()));
        promoModel.setStartDate(promoDO.getStartDate());
        promoModel.setEndDate(promoDO.getEndDate());

        return promoModel;
    }
}
