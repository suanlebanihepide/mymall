/*
 * @Author: shenzheng
 * @Date: 2020/6/18 2:10
 */

package com.example.mymmal.Controller.backend;

import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Service.PromoService;
import com.example.mymmal.Service.model.PromoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manage_promo")
public class PromoManage {

    @Autowired
    private PromoService promoService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/getPromo")
    @ResponseBody
    public CommonReturnType getPromo(@RequestParam("itemId") Integer itemId) {
        PromoModel list = promoService.getPromoByItemId(itemId);
        if (list != null) {
            return CommonReturnType.create(list);
        }
        return CommonReturnType.create(null);
    }

    @RequestMapping("/addPromo")
    @ResponseBody
    public CommonReturnType addPromo(@RequestParam("itemId") Integer itemId,
                                     @RequestParam("promoName") String promoName,
                                     @RequestParam("price") BigDecimal price,
                                     @RequestParam("startDate") String startDate,
                                     @RequestParam("endDate") String endDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<Integer, BigDecimal> map = new HashMap<>();
        map.put(itemId, price);
        Boolean res = promoService.submitPromo(promoName,map, df.parse(startDate), df.parse(endDate));
        if (res != null) {
            return CommonReturnType.create(null);
        }
        return CommonReturnType.create(null, "false");
    }

    @RequestMapping("/publishPromo")
    @ResponseBody
    public CommonReturnType publishPromo(@RequestParam("promoId") Integer promoId) {

        promoService.publishPromo(promoId);
        return CommonReturnType.create(null);
    }


}
