package com.example.mymmal.Service;

import com.example.mymmal.Service.model.ItemModel;

import java.util.List;

public interface ItemService {

    public List<ItemModel> getProductList(int pageNum, int pageSize);

    public ItemModel getProductDetails(Integer id);

    ItemModel createItemModel(ItemModel itemModel);


    ItemModel updateItemModel(ItemModel itemModel);


}
