/*
 * @Author: shenzheng
 * @Date: 2020/6/17 2:37
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.ShippingService;
import com.example.mymmal.dao.ShippingDOMapper;
import com.example.mymmal.dataobject.ShippingDO;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {

    @Autowired
    private ShippingDOMapper shippingDOMapper;

    @Override
    public Boolean add(Integer userId, ShippingDO shippingDO) {

        shippingDO.setUserId(userId);
        int res = shippingDOMapper.insertSelective(shippingDO);
        if (res > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean del(Integer userId, Integer shippingId) {
        int res = shippingDOMapper.deleteByPrimaryKey(shippingId);
        if (res > 0) {
            return true;
        }
        return false;
    }

    @Override
    public ShippingDO update(Integer userId, ShippingDO shippingDO) {

        shippingDO.setUserId(userId);

        int res = shippingDOMapper.updateByShipping(shippingDO);

        return null;
    }

    @Override
    public ShippingDO select(Integer userId, Integer shippingId) {

        ShippingDO shippingDO = shippingDOMapper.selectByPrimaryKey(shippingId);
        if (shippingDO == null) {
            return null;
        }

        return shippingDO;
    }

    @Override
    public List<ShippingDO> select(Integer userId) {

        List<ShippingDO> shippingList = shippingDOMapper.selectByUserId(userId);
        if (shippingList == null)
            return null;
        return shippingList;
    }
}
