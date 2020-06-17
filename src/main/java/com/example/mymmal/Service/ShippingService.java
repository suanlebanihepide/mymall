package com.example.mymmal.Service;

import com.example.mymmal.dataobject.ShippingDO;
import io.swagger.models.auth.In;

import java.util.List;

public interface ShippingService {

    Boolean add(Integer userId, ShippingDO shippingDO);

    Boolean del(Integer userId, Integer shippingId);

    ShippingDO update(Integer userId, ShippingDO shippingDO);

    ShippingDO select(Integer userId, Integer shippingId);

    List<ShippingDO> select(Integer userId);


}
