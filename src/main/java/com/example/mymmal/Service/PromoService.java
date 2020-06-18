/*
 * @Author: shenzheng
 * @Date: 2020/6/18 1:11
 */

package com.example.mymmal.Service;

import com.example.mymmal.Service.model.PromoModel;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PromoService {
    PromoModel getPromoByItemId(Integer itemId);

    Boolean submitPromo(String promoName,Map<Integer, BigDecimal> map, Date startTime,Date endTime);//提前存储一个秒杀的活动

    //活动发布
    void publishPromo(Integer promoId);//发布秒杀活动

    //生成秒杀用的令牌
    String generateSecondKillToken(Integer promoId, Integer itemId, Integer userId);

}
