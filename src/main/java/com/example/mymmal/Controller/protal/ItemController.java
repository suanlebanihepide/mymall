/*
 * @Author: shenzheng
 * @Date: 2020/6/16 3:51
 */

package com.example.mymmal.Controller.protal;

import com.example.mymmal.Controller.response.CommonReturnType;

import com.example.mymmal.Controller.vo.ItemVo;
import com.example.mymmal.Service.CacheService;
import com.example.mymmal.Service.ItemService;
import com.example.mymmal.Service.model.ItemModel;
import com.example.mymmal.dao.CategoryDOMapper;
import com.example.mymmal.dataobject.CategoryDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.NativeUint8Array;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/item")
@Api(tags = "前台商品接口", description = "通过前台商品接口负责查看商品列表和商品详情")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CategoryDOMapper categoryDOMapper;


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
