package com.example.mymmal.Service;

import com.example.mymmal.Service.model.ItemModel;

import java.util.List;

public interface ItemService {

    public List<ItemModel> getProductList(int pageNum, int pageSize);

    public ItemModel getProductDetails(Integer id);

    ItemModel createItemModel(ItemModel itemModel);


    //更新产品
    ItemModel updateItemModel(ItemModel itemModel);

    //查询产品细节
    ItemModel getItemModelById(Integer itemId);

    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount);

    //库存回补
    boolean increaseStock(Integer itemId, Integer amount);

    //异步更新库存
    boolean asyncDecreaseStock(Integer itemId, Integer amount);

    String initStackLog(Integer itemId, Integer amount);

    //增加商品销量
    void increaseSales(Integer itemId, Integer amount);


}
