/*
 * @Author: shenzheng
 * @Date: 2020/6/16 21:41
 */

package com.example.mymmal.Controller.protal;


import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.CartProductVo;
import com.example.mymmal.Controller.vo.CartVo;
import com.example.mymmal.Controller.vo.UserVo;
import com.example.mymmal.Service.CartService;
import com.example.mymmal.Service.model.CartModel;
import com.example.mymmal.config.error.EmBusinessError;
import com.example.mymmal.dao.ItemDOMapper;
import com.example.mymmal.dataobject.ItemDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/cart")
@Api(tags = "前台购物车接口", description = "用户购物车相关操作")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ItemDOMapper itemDOMapper;


    //购物车管理模块
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "购物车列表获取")
    public CommonReturnType list(HttpServletRequest request) {
        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;

        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<CartModel> list = cartService.getListByUserId(userVo.getId());
        if (list == null) return CommonReturnType.create("参数错误", "false");
        List<CartProductVo> productVos = convertFromCartModel(list);

        return CommonReturnType.create(convertFromCartProductVo(productVos));
    }


    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "添加购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "商品数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "productId", value = "商品编号", required = true, dataType = "Integer")})
    public CommonReturnType add(HttpServletRequest request, @RequestParam("count") Integer count, @RequestParam("productId") Integer productId) {
        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;

        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<CartModel> list = cartService.add(userVo.getId(), productId, count);
        if (list == null) return CommonReturnType.create("参数错误", "false");

        List<CartProductVo> productVos = convertFromCartModel(list);

        return CommonReturnType.create(convertFromCartProductVo(productVos));

    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "修改购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "商品数量", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "productId", value = "商品编号", required = true, dataType = "Integer")})

    public CommonReturnType updateCart(HttpServletRequest request, @RequestParam("count") Integer count, @RequestParam("productId") Integer productId) {
        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;

        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<CartModel> list = cartService.update(userVo.getId(), productId, count);
        if (list == null) return CommonReturnType.create("参数错误", "false");
        List<CartProductVo> productVos = convertFromCartModel(list);
        return CommonReturnType.create(convertFromCartProductVo(productVos));
    }

    @RequestMapping(value = "delete_product", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "删除购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品编号", required = true, dataType = "Integer")})

    public CommonReturnType deleteCart(HttpServletRequest request, @RequestParam("productId") String productId) {
        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;
        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<CartModel> list = cartService.deleteProduct(userVo.getId(), productId);
        if (list == null) return CommonReturnType.create("参数错误", "false");
        List<CartProductVo> productVos = convertFromCartModel(list);
        return CommonReturnType.create(convertFromCartProductVo(productVos));
    }

    @RequestMapping(value = "select", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "购物车商品选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "checked", value = "选中状态", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "productId", value = "商品编号", required = true, dataType = "Integer")})
    public CommonReturnType selectProduct(HttpServletRequest request, @RequestParam("productId") Integer productId,
                                          @RequestParam("checked") Integer checked) {
        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;
        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        List<CartModel> list = cartService.selectOrUnSelect(userVo.getId(), productId, checked);
        if (list == null) return CommonReturnType.create("参数错误", "false");
        List<CartProductVo> productVos = convertFromCartModel(list);
        return CommonReturnType.create(convertFromCartProductVo(productVos));
    }

    @RequestMapping(value = "selectAll", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "商品全选或反选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品编号", required = true, dataType = "Integer")})

    public CommonReturnType selectAllProduct(HttpServletRequest request, @RequestParam("checked") Integer checked) {
        Object t;
        if ((t = request.getSession().getAttribute("IS_LOGIN")) == null) {

            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        boolean is_Login = (boolean) t;
        if (is_Login == false) {
            return CommonReturnType.create(EmBusinessError.NOT_LOGIN.getErrMsg(), "false");
        }
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        List<CartModel> list = cartService.selectOrUnSelect(userVo.getId(), null, checked);
        if (list == null) return CommonReturnType.create("参数错误", "false");
        List<CartProductVo> productVos = convertFromCartModel(list);
        return CommonReturnType.create(convertFromCartProductVo(productVos));
    }


    private List<CartProductVo> convertFromCartModel(List<CartModel> list) {

        List<CartProductVo> res = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {

            CartModel cartModel = list.get(i);
            CartProductVo cartProductVo = new CartProductVo();
            BeanUtils.copyProperties(cartModel, cartProductVo);
            ItemDO itemDO = itemDOMapper.selectByPrimaryKey(cartModel.getProductId());
            cartProductVo.setProductName(itemDO.getTitle());
            cartProductVo.setPrice(new BigDecimal(itemDO.getPrice()));
            cartProductVo.setTotal(cartProductVo.getPrice().multiply(new BigDecimal(cartModel.getQuantity())));
            res.add(cartProductVo);
        }
        return res;
    }

    private CartVo convertFromCartProductVo(List<CartProductVo> list) {
        CartVo cartVo = new CartVo();

        cartVo.setCartProductVoList(list);
        cartVo.setAllChecked(true);

        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < list.size(); i++) {
            CartProductVo cartProductVo = list.get(i);
            if (cartProductVo.getChecked() != 1) {
                cartVo.setAllChecked(false);
                continue;
            }
            sum = sum.add(cartProductVo.getTotal());
        }
        cartVo.setCartTotalPrice(sum);

        return cartVo;
    }

}
