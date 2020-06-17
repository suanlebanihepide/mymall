/*
 * @Author: shenzheng
 * @Date: 2020/6/17 1:57
 */

package com.example.mymmal.Controller.backend;

import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Service.CategoryService;
import com.example.mymmal.dataobject.CategoryDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/manage/category")
@Api(tags = "后台商品类别CRUD接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "add_category", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加商品类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryName", value = "类别名称", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "parentId", value = "父类编号", required = false, dataType = "Integer")})

    public CommonReturnType addCategory(HttpServletRequest request, @RequestParam(value = "categoryName") String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        CategoryDO categoryDO = categoryService.addCategory(categoryName, parentId);
        if (categoryDO != null) {
            return CommonReturnType.create(categoryDO);
        }
        return CommonReturnType.create(null, "false");
    }

    @RequestMapping(value = "update_category", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新商品类别")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryDO", value = "分类", required = false, dataType = "Integer")})

    public CommonReturnType setCategoryName(CategoryDO categoryDO) {
        return CommonReturnType.create(categoryService.updateCategory(categoryDO));
    }

    @RequestMapping(value = "get_category", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取子商品类别")
    public CommonReturnType getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        List<CategoryDO> list = categoryService.getChildrenParallelCategory(categoryId);
        return CommonReturnType.create(list);
    }

    @RequestMapping(value = "get_deep_category", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取递归子类商品类别")
    public CommonReturnType getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        return CommonReturnType.create(categoryService.selectCategoryAndChildrenById(categoryId));
    }


}
