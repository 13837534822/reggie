package com.reggie.controller;


import com.reggie.common.Result;
import com.reggie.po.Orders;
import com.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping ("/submit")
    public Result<String> submit(@RequestBody Orders orders, HttpServletRequest  request) {
        orders.setUserId((Long) request.getSession().getAttribute("user"));
        ordersService.submit(orders);

        return Result.success("下单成功");
    }
}
