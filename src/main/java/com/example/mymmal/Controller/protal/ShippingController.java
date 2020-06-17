/*
 * @Author: shenzheng
 * @Date: 2020/6/17 16:54
 */

package com.example.mymmal.Controller.protal;


import com.example.mymmal.Controller.response.CommonReturnType;
import com.example.mymmal.Controller.vo.UserVo;
import com.example.mymmal.Service.ShippingService;
import com.example.mymmal.dataobject.ShippingDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/shipping")
public class ShippingController {


    @Autowired
    private ShippingService shippingService;

    @RequestMapping("add")
    @ResponseBody
    public CommonReturnType add(HttpServletRequest request, ShippingDO shippingDO) {
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        Boolean res = shippingService.add(userVo.getId(), shippingDO);
        if (res == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(shippingService.select(userVo.getId(), shippingDO.getId()));
    }

    @RequestMapping("del")
    @ResponseBody
    public CommonReturnType del(HttpServletRequest request, Integer shippingId) {
        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        Boolean res = shippingService.del(userVo.getId(), shippingId);
        if (res != null) {
            return CommonReturnType.create(null);
        }
        return CommonReturnType.create(null, "false");
    }

    @RequestMapping("list")
    @ResponseBody
    public CommonReturnType list(HttpServletRequest request) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        List<ShippingDO> list = shippingService.select(userVo.getId());
        if (list == null)
            return CommonReturnType.create(null, "false");
        return CommonReturnType.create(list);
    }

    @RequestMapping("select")
    @ResponseBody
    public CommonReturnType select(HttpServletRequest request, Integer shippingId) {


        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");

        ShippingDO shippingDO = shippingService.select(userVo.getId(), shippingId);

        if (shippingDO != null) {
            return CommonReturnType.create(shippingDO);
        }
        return CommonReturnType.create(null);
    }

    @RequestMapping("update")
    @ResponseBody
    public CommonReturnType update(HttpServletRequest request, ShippingDO shipping) {

        UserVo userVo = (UserVo) request.getSession().getAttribute("LOGIN_USER");
        ShippingDO shippingDO = shippingService.update(userVo.getId(), shipping);

        if (shippingDO == null) {
            return CommonReturnType.create(null, "false");
        }
        return CommonReturnType.create(shippingDO);
    }


}
