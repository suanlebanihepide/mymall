/*
 * @Author: shenzheng
 * @Date: 2020/6/16 17:04
 */

package com.example.mymmal.Controller.backend;

import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.ItemVo;
import com.example.mymmal.Service.CacheService;
import com.example.mymmal.Service.ItemService;
import com.example.mymmal.Service.model.ItemModel;
import com.example.mymmal.dao.CategoryDOMapper;
import com.example.mymmal.dao.ItemDOMapper;
import com.example.mymmal.dataobject.CategoryDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/manage/product")
@Api(tags = "后台商品接口", description = "负责后台商品的CRUD以及秒杀活动发布")
public class ItemManageController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CategoryDOMapper categoryDOMapper;


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "description", value = "描述", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "stock", value = "库存", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "imgUrl", value = "描述图片", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "CategoryId", value = "类别", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "上架状态", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "sales", value = "销量", required = true, dataType = "Integer")})
    public CommonReturnType createItem(@RequestParam("title") String title,
                                       @RequestParam("description") String description,
                                       @RequestParam("price") BigDecimal price,
                                       @RequestParam("stock") Integer stock,
                                       @RequestParam("imgUrl") String imgUrl,
                                       @RequestParam("CategoryId") Integer CategoryId,
                                       @RequestParam("status") Integer status,
                                       @RequestParam("sales") Integer sales,
                                       HttpServletRequest request) {
        Boolean t;
        if ((t = (Boolean) request.getSession().getAttribute("IS_ADI_LOGIN")) == null) {
            return CommonReturnType.create("管理员未登录", "false");
        }

        if (!t) {
            return CommonReturnType.create("管理员未登录", "false");
        }
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        itemModel.setStatus(status);
        itemModel.setCategory_id(CategoryId);
        itemModel.setSales(sales);
        ItemModel itemModelForReturn = itemService.createItemModel(itemModel);
        ItemVo itemVo = convertVoFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVo, "success");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "下架商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "下架商品编号", required = true, dataType = "Integer")})
    public CommonReturnType updateItem(@RequestParam(value = "id", required = true) Integer id, HttpServletRequest request) {

        Boolean t;
        if ((t = (Boolean) request.getSession().getAttribute("IS_LOGIN")) == null) {
            return CommonReturnType.create("管理员未登录", "false");
        }
        if (!t) {
            return CommonReturnType.create("管理员未登录", "false");
        }
        ItemModel itemModel = new ItemModel();
        itemModel.setStatus(0);
        itemModel.setId(id);

        ItemModel itemModelForReturn = itemService.updateItemModel(itemModel);
        ItemVo itemVo = convertVoFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVo, "success");

    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品编号", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "title", value = "标题", required = false, dataType = "String"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, dataType = "String"),
            @ApiImplicitParam(name = "price", value = "价格", required = false, dataType = "BigDecimal"),
            @ApiImplicitParam(name = "stock", value = "库存", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "imgUrl", value = "描述图片", required = false, dataType = "String"),
            @ApiImplicitParam(name = "CategoryId", value = "类别", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "status", value = "上架状态", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "sales", value = "销量", required = false, dataType = "Integer")})
    public CommonReturnType updateItem(@RequestParam(value = "id", required = true) Integer id,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "description", required = false) String description,
                                       @RequestParam(value = "price", required = false) BigDecimal price,
                                       @RequestParam(value = "stock", required = false) Integer stock,
                                       @RequestParam(value = "imgUrl", required = false) String imgUrl,
                                       @RequestParam(value = "CategoryId", required = false) Integer CategoryId,
                                       @RequestParam(value = "status", required = false) Integer status,
                                       @RequestParam(value = "sales", required = false) Integer sales,
                                       HttpServletRequest request) {
        Boolean t;
        if ((t = (Boolean) request.getSession().getAttribute("IS_LOGIN")) == null) {
            return CommonReturnType.create("管理员未登录", "false");
        }

        if (!t) {
            return CommonReturnType.create("管理员未登录", "false");
        }
        ItemModel itemModel = new ItemModel();
        if (title != null) {
            itemModel.setTitle(title);
        }
        if (description != null) {
            itemModel.setDescription(description);
        }
        if (price != null) {
            itemModel.setPrice(price);
        }
        if (stock != null) {
            itemModel.setStock(stock);
        }
        if (imgUrl != null) {
            itemModel.setImgUrl(imgUrl);
        }
        if (status != null) {
            itemModel.setStatus(status);
        }
        if (CategoryId != null) {
            itemModel.setCategory_id(CategoryId);
        }
        if (sales != null) {
            itemModel.setSales(sales);
        }
        itemModel.setId(id);
        ItemModel itemModelForReturn = itemService.updateItemModel(itemModel);
        ItemVo itemVo = convertVoFromModel(itemModelForReturn);
        return CommonReturnType.create(itemVo, "success");
    }


    //商品列表页
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "商品分页获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "页面大小", required = true, dataType = "Integer")})

    public CommonReturnType list(@RequestParam(name = "pageNum", defaultValue = "1", required = false) Integer pageNum,
                                 @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize) {

        List<ItemModel> lists = itemService.getProductList(pageNum, pageSize);
        return CommonReturnType.create(lists);
    }

    //商品详情页
    @RequestMapping(value = "/getDetails", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "商品详情获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品编号", required = true, dataType = "Integer")})

    public CommonReturnType getDetails(@RequestParam(name = "id") Integer id) {
        ItemVo itemVo = null;
        //多集缓存1.本地2.redis 3.数据库
        itemVo = (ItemVo) cacheService.getFromCommonCache("item_" + id);
        if (itemVo == null) {
            itemVo = (ItemVo) redisTemplate.opsForValue().get("item_" + id);
            if (itemVo == null) {
                ItemModel itemModel = itemService.getProductDetails(id);
                itemVo = convertVoFromModel(itemModel);
                redisTemplate.opsForValue().set("item_" + id, itemVo);
                //设置有效时间
                redisTemplate.expire("item_" + id, 10, TimeUnit.MINUTES);
            }
            cacheService.setCommonCache("item_" + id, itemVo);
        }
        return CommonReturnType.create(itemVo);
    }


    private ItemVo convertVoFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVo itemVO = new ItemVo();
        BeanUtils.copyProperties(itemModel, itemVO);
        CategoryDO categoryDO = categoryDOMapper.selectByPrimaryKey(itemModel.getCategory_id());
        itemVO.setCategory(categoryDO.getName());
        if (itemModel.getPromoModel() != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(df.format(itemModel.getPromoModel().getStartDate()));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            itemVO.setPromoStatus(0);
        }

        return itemVO;
    }

}
