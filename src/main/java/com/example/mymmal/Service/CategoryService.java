package com.example.mymmal.Service;

import com.example.mymmal.dataobject.CategoryDO;

import java.util.List;

public interface CategoryService {

    //添加商品类别
    CategoryDO addCategory(String CategoryName, Integer parentId);

    //更新商品类别
    CategoryDO updateCategory(CategoryDO categoryDO);

    //查询当前类别的子分类
    List<CategoryDO> getChildrenParallelCategory(Integer categoryId);

    //查询当前类别和子类
    List<Integer> selectCategoryAndChildrenById(Integer categoryId);
}
