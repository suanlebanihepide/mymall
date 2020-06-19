package com.example.mymmal.Service;

import com.example.mymmal.Service.model.OrderItemModel;
import com.example.mymmal.Service.model.OrderModel;
import com.example.mymmal.dataobject.OrderInfoDO;

import java.util.List;

public interface OrderService {

    //前端
    OrderModel createOrder(Integer userId, Integer shippingId);

    //1.通过前端url上传秒杀活动Id,校验对应id的对应商品是否已经开始
    //2.直接下单接口判断对应商品是否有秒杀活动，有则按照秒杀价格下单
    OrderItemModel createOrder_promo(Integer userId, Integer itemId, Integer promoId, Integer amount, Integer shippingId);

    List<OrderInfoDO> getOrderList(Integer userId, int pageNum, int pageSize);

    OrderInfoDO getOrderDetails(Integer userId, String orderNo);

    //取消订单
    Boolean cancel(Integer userId, String orderNo);




    //backend
    //查看订单列表
    List<OrderInfoDO> manageList(int pageNum, int pageSize);

    //查看订单详情
    OrderInfoDO manageDetail(String orderNo);

    //发货
    OrderInfoDO manageSendGoods(String orderNo);


    //Hour个小时内如果没有支付进行关单操作
    void closeOrder(int hour);
}
