/*
 * @Author: shenzheng
 * @Date: 2020/6/18 1:57
 */

package com.example.mymmal.Controller.protal;

import com.alibaba.druid.util.StringUtils;
import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.UserVo;
import com.example.mymmal.Service.PromoService;
import com.example.mymmal.Service.model.PromoModel;
import com.example.mymmal.Service.model.UserModel;
import com.example.mymmal.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/promo")
public class PromoController {

    @Autowired
    private PromoService promoService;
    @Autowired
    private RedisTemplate redisTemplate;


    //生成验证码
    @RequestMapping(value = "/generateVerifyCode",method = RequestMethod.GET)
    @ResponseBody
    public void generateVerifyCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        Map<String, Object> map = CodeUtil.generateCodeAndPic();
        redisTemplate.opsForValue().set("verifyCode_" + userVo.getId(), map.get("code"));
        redisTemplate.expire("verifyCode_" + userVo.getId(), 5, TimeUnit.MINUTES);
        ImageIO.write((RenderedImage) map.get("codePic"), "jpeg", response.getOutputStream());
    }

    @RequestMapping(value = "/getPromo", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getPromo(@RequestParam("itemId") Integer itemId) {

        return CommonReturnType.create(promoService.getPromoByItemId(itemId));
    }


    //封装下单请求
    @RequestMapping(value = "/generateToken",method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType generateToken(@RequestParam("itemId") Integer itemId,
                                          @RequestParam(value = "promoId", required = false) Integer promoId,
                                          @RequestParam("verifyCode") String verifyCode,
                                          HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        String promo_token = promoService.generateSecondKillToken(promoId, itemId, userVo.getId());
        String str_verifyCode = (String) redisTemplate.opsForValue().get("verifyCode_" + userVo.getId());
        if (!StringUtils.equals(str_verifyCode, verifyCode) && !StringUtils.isEmpty(verifyCode)) {
            return CommonReturnType.create("用户未登录", "false");
        }

        if (promo_token == null) {
            return CommonReturnType.create("抢购结束", "false");
        }
        return CommonReturnType.create(promo_token);
    }


}
