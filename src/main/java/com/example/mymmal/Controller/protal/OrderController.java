/*
 * @Author: shenzheng
 * @Date: 2020/6/18 23:02
 */

package com.example.mymmal.Controller.protal;

import com.alibaba.druid.util.StringUtils;
import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.UserVo;
import com.example.mymmal.Service.ItemService;
import com.example.mymmal.Service.OrderService;
import com.example.mymmal.Service.PromoService;
import com.example.mymmal.Service.model.OrderItemModel;
import com.example.mymmal.Service.model.OrderModel;
import com.example.mymmal.Service.model.UserModel;
import com.example.mymmal.dataobject.OrderInfoDO;
import com.example.mymmal.util.CodeUtil;
import com.google.common.util.concurrent.RateLimiter;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PromoService promoService;
    private ExecutorService executorService;

    private RateLimiter orderCreateRateLimiter;

    @PostConstruct
    private void init() {

        executorService = Executors.newFixedThreadPool(20);
        orderCreateRateLimiter = RateLimiter.create(300);
    }


    //生成验证码
    @RequestMapping(value = "/generateVerifyCode", method = RequestMethod.GET)
    @ResponseBody
    public void generateVerifyCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        Map<String, Object> map = CodeUtil.generateCodeAndPic();
        redisTemplate.opsForValue().set("verifyCode_" + userVo.getId(), map.get("code"));
        redisTemplate.expire("verifyCode_" + userVo.getId(), 5, TimeUnit.MINUTES);
        ImageIO.write((RenderedImage) map.get("codePic"), "jpeg", response.getOutputStream());
    }

    //封装下单请求
    @RequestMapping("/generateToken")
    @ResponseBody
    public CommonReturnType generateToken(@RequestParam("itemId") Integer itemId,
                                          @RequestParam(value = "promoId", required = false) Integer promoId,
                                          @RequestParam("token") String token,
                                          @RequestParam("verifyCode") String verifyCode,
                                          HttpServletRequest request) {


        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        String promo_token = promoService.generateSecondKillToken(promoId, itemId, userVo.getId());

        String str_verifyCode = (String) redisTemplate.opsForValue().get("verifyCode_" + userVo.getId());
        if (!StringUtils.equals(str_verifyCode, verifyCode) && !StringUtils.isEmpty(verifyCode)) {
            return CommonReturnType.create("验证码错误", "false");
        }
        if (promo_token == null) {
            return CommonReturnType.create("令牌发放完毕", "false");
        }
        return CommonReturnType.create(promo_token);
    }


    //封装下单请求
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(value = "ShippingId", required = false) Integer ShippingId,
                                        HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userVo.getId(), ShippingId);
        if (orderModel == null) {

            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(orderModel);
    }

    //封装下单请求
    @RequestMapping(value = "/getOrder")
    @ResponseBody
    public CommonReturnType getOrder(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "2") Integer pageSize,
                                     HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<OrderInfoDO> list = orderService.getOrderList(userVo.getId(), pageNum, pageSize);
        if (list == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(list);
    }

    //封装下单请求
    @RequestMapping(value = "/getOrderDetails", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getOrderDetails(@RequestParam(value = "OrderNo", required = false) String OrderNo,
                                            HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        OrderInfoDO res = orderService.getOrderDetails(userVo.getId(), OrderNo);
        if (res == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(res);
    }

    //封装下单请求
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public CommonReturnType cancel(@RequestParam(value = "OrderNo", required = false) String OrderNo,
                                   HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        Boolean res = orderService.cancel(userVo.getId(), OrderNo);
        if (res == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(res);
    }

    //封装下单请求
    @RequestMapping("/createOrderPromo")
    @ResponseBody
    public CommonReturnType createOrderPromo(@RequestParam("itemId") Integer itemId,
                                             @RequestParam(value = "promoId", required = false) Integer promoId,
                                             @RequestParam("amount") Integer amount,
                                             @RequestParam(value = "promo_token", required = false) String promo_token,
                                             @RequestParam(value = "ShippingId", required = false) Integer ShippingId,
                                             HttpServletRequest request) {

        //令牌桶
        if (orderCreateRateLimiter.acquire() < 0) {
            return CommonReturnType.create("令牌桶发放完毕", "false");

        }


        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        //校验秒杀令牌是否正确
        if (promoId != null) {
            String inRedisPromotoken = (String) redisTemplate.opsForValue().get("promo_token" + promoId + "user_id" + userVo.getId() + "itemId" + itemId);
            if (inRedisPromotoken == null) {
                return CommonReturnType.create("秒杀校验失败", "false");
            }
            if (!StringUtils.equals(promo_token, inRedisPromotoken)) {
                return CommonReturnType.create("秒杀校验失败", "false");
            }
        }
        //队列泄洪
        Future<Object> future = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                itemService.initStackLog(itemId, amount);
                OrderItemModel orderModel = orderService.createOrder_promo(userVo.getId(), itemId, promoId, amount, ShippingId);
                return null;
            }
        });

        try {
            future.get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
        return CommonReturnType.create(null);
    }
}
