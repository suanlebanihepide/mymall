/*
 * @Author: shenzheng
 * @Date: 2020/6/17 1:44
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.CategoryService;
import com.example.mymmal.dao.CategoryDOMapper;
import com.example.mymmal.dataobject.CategoryDO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDOMapper categoryDOMapper;

    @Override
    public CategoryDO addCategory(String CategoryName, Integer parentId) {

        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setName(CategoryName);
        categoryDO.setParentId(parentId);
        categoryDO.setStatus(true);//判断分类是否可用
        int res = categoryDOMapper.insertSelective(categoryDO);
        if (res > 0) {
            return categoryDO;
        }
        return null;
    }

    @Override
    public CategoryDO updateCategory(CategoryDO categoryDO) {

        categoryDOMapper.updateByPrimaryKeySelective(categoryDO);
        return categoryDOMapper.selectByPrimaryKey(categoryDO.getId());
    }

    @Override
    public List<CategoryDO> getChildrenParallelCategory(Integer categoryId) {
        List<CategoryDO> categoryList = categoryDOMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            return null;
        }
        return categoryList;

    }
    @Override
    public List<Integer> selectCategoryAndChildrenById(Integer categoryId) {
        //递归查询所有子节点
        Set<CategoryDO> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            for (CategoryDO categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return categoryIdList;
    }

    //递归算法,算出子节点
    private Set<CategoryDO> findChildCategory(Set<CategoryDO> categorySet, Integer categoryId) {
        CategoryDO category = categoryDOMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找子节点
        List<CategoryDO> categoryList = categoryDOMapper.selectCategoryChildrenByParentId(categoryId);
        for (CategoryDO categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
