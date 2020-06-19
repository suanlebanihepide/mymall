/*
 * @Author: shenzheng
 * @Date: 2020/6/18 1:10
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.ItemService;
import com.example.mymmal.Service.OrderService;
import com.example.mymmal.Service.UserService;
import com.example.mymmal.Service.model.ItemModel;
import com.example.mymmal.Service.model.OrderItemModel;
import com.example.mymmal.Service.model.OrderModel;
import com.example.mymmal.dao.*;
import com.example.mymmal.dataobject.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderInfoDOMapper orderInfoDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private CartDOMapper cartDOMapper;
    @Autowired
    private ShippingDOMapper shippingDOMapper;

    @Override
    public OrderModel createOrder(Integer userId, Integer shippingId) {


        List<CartDO> list = cartDOMapper.selectCartByUserId(userId);

        OrderModel result = new OrderModel();
        result.setTotal(new BigDecimal(0));
        result.setOrderProductList(new ArrayList<>());
        for (CartDO cartDO : list) {

            ItemModel itemModel = itemService.getProductDetails(cartDO.getProductId());
            if (itemModel == null) continue;
            //减库存
            boolean mqResult = itemService.asyncDecreaseStock(itemModel.getId(), cartDO.getQuantity());
            //3.订单入库
            OrderItemModel orderModel = new OrderItemModel();
            orderModel.setUserId(userId);
            orderModel.setItemId(cartDO.getProductId());
            orderModel.setAmount(cartDO.getQuantity());
            orderModel.setItemPrice(itemModel.getPrice());
            orderModel.setOrderAmount(orderModel.getItemPrice().multiply(new BigDecimal(cartDO.getQuantity())));
            orderModel.setId(generateOrderNo());
            orderModel.setStatus(10);

            result.getOrderProductList().add(orderModel);
            BigDecimal tmp = result.getTotal();
            result.setTotal(tmp.add(orderModel.getOrderAmount()));


            OrderInfoDO orderDO = covertFromOrderModel(orderModel);
            orderDO.setShippingId(shippingId);
            orderDO.setCreateTime(new Date());
            orderDO.setUpdateTime(orderDO.getCreateTime());
            orderDO.setCloseTime(new Date());
            orderDO.setEndTime(new Date());
            orderDO.setEndTime(new Date());
            orderDO.setPaymentTime(new Date());
            orderDO.setSendTime(new Date());
            orderDO.setPromoId(-1);
            //生成交易流水号
            orderInfoDOMapper.insert(orderDO);
            //加上商品销量
            itemService.increaseSales(cartDO.getProductId(), cartDO.getQuantity());
        }

        result.setStatus(10);
        result.setSend_time(new Date().toString());
        result.setShippingDO(shippingDOMapper.selectByPrimaryKey(shippingId));

        return result;
    }

    @Override
    @Transactional
    public OrderItemModel createOrder_promo(Integer userId, Integer itemId, Integer promoId, Integer amount, Integer shippingId) {
        //1.校验下单状态，下单商品是否存在
        ItemModel itemModel = itemService.getProductDetails(itemId);
        if (itemModel == null) {
            return null;
        }
        if (amount <= 0 || amount >= 99) {
            return null;
        }
        //2.落单减库存，支付减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            return null;
        }
//3.订单入库
        OrderItemModel orderModel = new OrderItemModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);

        if (promoId != null) {
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setOrderAmount(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
        orderModel.setPromoId(promoId);
        orderModel.setId(generateOrderNo());

        orderModel.setStatus(10);
        OrderInfoDO orderDO = covertFromOrderModel(orderModel);
        orderDO.setShippingId(shippingId);
        orderDO.setCreateTime(new Date());
        orderDO.setUpdateTime(orderDO.getCreateTime());

        orderDO.setCloseTime(new Date());
        orderDO.setEndTime(new Date());
        orderDO.setEndTime(new Date());
        orderDO.setPaymentTime(new Date());
        orderDO.setSendTime(new Date());
        //生成交易流水号
        orderInfoDOMapper.insert(orderDO);
        //加上商品销量
        itemService.increaseSales(itemId, amount);
        //最近一个Transactional执行完成后才会执行代码
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

            @SneakyThrows
            @Override
            public void afterCommit() {
                //异步更新库存
                boolean mqResult = itemService.asyncDecreaseStock(itemId, amount);
//                if (!mqResult) {
//
//                    itemService.increaseStock(itemId, amount);
//                    throw new BusinessException(EmBusinessError.MQ_SEND_FAIL);
//                }
            }
        });
        return orderModel;
    }


    @Override
    public List<OrderInfoDO> getOrderList(Integer userId, int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        List<OrderInfoDO> list = orderInfoDOMapper.listItemByUserId(userId, start, pageSize);
        if (list == null) return null;
        List<OrderInfoDO> list2 = list.subList(list.size() - pageSize, list.size());
        return list2;
    }

    @Override
    public OrderInfoDO getOrderDetails(Integer userId, String orderNo) {

        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(orderNo);

        return orderInfoDO;
    }

    @Override
    public Boolean cancel(Integer userId, String orderNo) {
        Boolean res = orderInfoDOMapper.updateStatusByOrderNo(userId, orderNo);
        return res;
    }

    @Override
    public List<OrderInfoDO> manageList(int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        List<OrderInfoDO> list = orderInfoDOMapper.listItem(start, pageSize);
        if (list == null) return null;
        List<OrderInfoDO> list2 = list.subList(list.size() - pageSize, list.size());
        return list2;
    }

    @Override
    public OrderInfoDO manageDetail(String orderNo) {

        return orderInfoDOMapper.selectByPrimaryKey(orderNo);
    }

    @Override
    public OrderInfoDO manageSendGoods(String orderNo) {
        OrderInfoDO orderInfoDO = orderInfoDOMapper.selectByPrimaryKey(orderNo);
        orderInfoDO.setSendTime(new Date());

        int res = orderInfoDOMapper.updateByPrimaryKeySelective(orderInfoDO);
        if (res < 0) return null;
        return orderInfoDO;
    }


    @Override
    public void closeOrder(int hour) {

        Date closeDateTime = DateUtils.addHours(new Date(), -hour);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        List<OrderInfoDO> orderInfoDOList = orderInfoDOMapper.selectOrderStatusByCreateTime(10, df.format(closeDateTime));

        for (OrderInfoDO t : orderInfoDOList) {

            //使用主键防止表锁
            itemDOMapper.selectByPrimaryKey(t.getItemId());
//            if (itemDO == null) {
//                continue;
//            }
            //商品回补
            itemService.increaseStock(t.getItemId(), t.getAmount());//回补缓存
            itemDOMapper.increaseStock(t.getItemId(), t.getAmount());//回补数据库

            orderInfoDOMapper.closeOrderByOrderId(t.getId());
        }

    }

    //在已有事务中开启一个新的事务，确保不会因为上层事务回滚导致发生数据更改

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected String generateOrderNo() {
        //订单号16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息 年月日

        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");

        stringBuilder.append(nowDate);
        //中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getStep() + sequenceDO.getCurrentValue());
        sequenceDOMapper.updateByPrimaryKey(sequenceDO);
        String sequenceString = String.valueOf(sequence);

        for (int i = 0; i < 6 - sequenceString.length(); i++) {
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceString);

        //后两位分库分表位
        stringBuilder.append("00");

        return stringBuilder.toString();
    }

    private OrderInfoDO covertFromOrderModel(OrderItemModel orderItemModel) {
        if (orderItemModel == null) return null;
        OrderInfoDO orderDO = new OrderInfoDO();
        BeanUtils.copyProperties(orderItemModel, orderDO);
        orderDO.setItemPrice(orderItemModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderItemModel.getOrderAmount().doubleValue());
        orderDO.setPromoId(orderItemModel.getPromoId());
        return orderDO;
    }


}
