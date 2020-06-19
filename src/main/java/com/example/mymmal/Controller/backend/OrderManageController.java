/*
 * @Author: shenzheng
 * @Date: 2020/6/19 18:25
 */

package com.example.mymmal.Controller.backend;

import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.UserVo;
import com.example.mymmal.Service.OrderService;
import com.example.mymmal.dataobject.OrderInfoDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/order_manage")
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    //封装下单请求
    @RequestMapping(value = "/getOrder")
    @ResponseBody
    public CommonReturnType getOrder(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "2") Integer pageSize,
                                     HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<OrderInfoDO> list = orderService.manageList(pageNum, pageSize);
        if (list == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(list);
    }

    //封装下单请求
    @RequestMapping(value = "/getOrderDetail")
    @ResponseBody
    public CommonReturnType getOrder(@RequestParam(value = "OrderNo", required = false) String OrderNo,
                                     HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        OrderInfoDO res = orderService.manageDetail(OrderNo);
        if (res == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(res);
    }

    //封装下单请求
    @RequestMapping(value = "/manageSendGoods")
    @ResponseBody
    public CommonReturnType manageSendGoods(@RequestParam(value = "OrderNo", required = false) String OrderNo,
                                            HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        OrderInfoDO res = orderService.manageSendGoods(OrderNo);
        if (res == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(res);
    }


}
