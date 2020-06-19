/*
 * @Author: shenzheng
 * @Date: 2020/6/16 3:29
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.ItemService;
import com.example.mymmal.Service.PromoService;
import com.example.mymmal.Service.model.ItemModel;
import com.example.mymmal.Service.model.PromoModel;
import com.example.mymmal.dao.CategoryDOMapper;
import com.example.mymmal.dao.ItemDOMapper;
import com.example.mymmal.dao.ItemStockDOMapper;

import com.example.mymmal.dataobject.ItemDO;
import com.example.mymmal.dataobject.ItemStockDO;

import com.example.mymmal.dataobject.PromoDO;
import com.example.mymmal.mq.MqProducer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Autowired
    private CategoryDOMapper categoryDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PromoService promoService;
    @Autowired
    private MqProducer producer;

    @Override
    public List<ItemModel> getProductList(int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        List<ItemDO> list = itemDOMapper.listItem(start, pageSize);

        List<ItemDO> list2 = list.subList(list.size() - pageSize, list.size());
        List<ItemModel> itemModels = list2.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());


        return itemModels;
    }

    @Override
    public ItemModel getProductDetails(Integer id) {
        ItemModel itemModel = null;
        itemModel = (ItemModel) redisTemplate.opsForValue().get("item_model_" + id);
        if (itemModel == null) {
            ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
            if (itemDO == null) return null;
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            itemModel = convertModelFromDataObject(itemDO, itemStockDO);
            //添加活动模块
            PromoModel list = promoService.getPromoByItemId(id);
            itemModel.setPromoModel(list);
            redisTemplate.opsForValue().set("item_model_" + id, itemModel);
            redisTemplate.expire("item_model_" + id, 10, TimeUnit.MINUTES);

        }
        return itemModel;
    }


    @Override
    public ItemModel createItemModel(ItemModel itemModel) {

        //检验参数的合法性
        ItemDO itemDO = convertItemDOFromItemModel(itemModel);

        //设置itemDo的主键否则根据该主键奇偶来进行分表
        itemDO.setId(999);
        itemModel.setId(999);
        itemDOMapper.insert(itemDO);
        ItemStockDO itemStockDO = convertItemStockDoFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        return getProductDetails(itemDO.getId());
    }

    @Override
    public ItemModel updateItemModel(ItemModel itemModel) {

        //检验参数的合法性
        ItemDO itemDO = convertItemDOFromItemModel(itemModel);

        itemDO.setId(itemModel.getId());

        itemDOMapper.updateByPrimaryKeySelective(itemDO);


        ItemStockDO itemStockDO = convertItemStockDoFromItemModel(itemModel);
        itemStockDO.setId(itemStockDOMapper.selectByItemId(itemModel.getId()).getId());
        itemStockDOMapper.updateByPrimaryKeySelective(itemStockDO);

        //更新缓存中的数据
        ItemDO itemDO_update = itemDOMapper.selectByPrimaryKey(itemModel.getId());
        ItemStockDO itemStockDO_update = itemStockDOMapper.selectByItemId(itemDO_update.getId());
        ItemModel update_itemModel_after = convertModelFromDataObject(itemDO_update, itemStockDO_update);
        redisTemplate.opsForValue().set("item_model_" + itemModel.getId(), update_itemModel_after);
        redisTemplate.expire("item_model_" + itemModel.getId(), 10, TimeUnit.MINUTES);
        return update_itemModel_after;
    }


    @Override
    public ItemModel getItemModelById(Integer itemId) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(itemId);
        if (itemDO == null) {
            return null;
        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());

        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);
        //将dataobject->model

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        //返回剩下的库存数字
        Long result = redisTemplate.opsForValue().increment("promo_item_stock" + itemId, -1 * amount);

        if (result < 0) {
            increaseStock(itemId, amount);

            return false;
        } else if (result == 0) {
            redisTemplate.opsForValue().set("promo_item_stock_validate" + itemId, "true");

        }
        return true;
    }

    @Override
    public boolean increaseStock(Integer itemId, Integer amount) {
        redisTemplate.opsForValue().increment("promo_item_stock" + itemId, amount);
        return true;
    }

    @Override
    public boolean asyncDecreaseStock(Integer itemId, Integer amount) {
        Boolean msgResult = producer.asyncReduceStock(itemId, amount);
        return msgResult;
    }

    @Override
    public String initStackLog(Integer itemId, Integer amount) {
//        StockLogDO stockLogDO = new StockLogDO();
//        stockLogDO.setItemId(itemId);
//        stockLogDO.setAmount(amount);
//        stockLogDO.setStockLogId(UUID.randomUUID().toString().replaceAll("-", ""));
//        stockLogDO.setStatus(1);
//        stockLogDOMapper.insert(stockLogDO);
//        return stockLogDO.getStockLogId();
        return "";
    }

    @Override
    public void increaseSales(Integer itemId, Integer amount) {
        itemDOMapper.increaseSales(itemId, amount);
    }


    private ItemStockDO convertItemStockDoFromItemModel(ItemModel itemModel) {

        if (itemModel == null) return null;
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        if (itemModel.getStock() != null)
            itemStockDO.setStock(itemModel.getStock());
        if (itemModel.getSales() != null)
            itemStockDO.setSales(itemModel.getSales());
        return itemStockDO;
    }

    private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {

        if (itemModel == null) return null;
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        if (itemModel.getPrice() != null)
            itemDO.setPrice(itemModel.getPrice().doubleValue());
        if (itemModel.getCategory_id() != null)
            itemDO.setCategoryId(itemModel.getCategory_id());

        return itemDO;
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        ItemModel itemModel = new ItemModel();
        if (itemDO == null || itemStockDO == null) return null;

        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        itemModel.setCategory_id(itemDO.getCategoryId());
        return itemModel;
    }
}
