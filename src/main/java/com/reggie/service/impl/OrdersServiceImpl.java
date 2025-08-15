package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.MyException;
import com.reggie.mapper.OrdersMapper;
import com.reggie.po.*;
import com.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService{

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private DishService dishService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public void submit(Orders orders) {
        User user=userService.getById(orders.getUserId());

        Long order_id=IdWorker.getId(); //为一个订单创建一个id

        //查询当前用户购物车数据
        QueryWrapper<ShoppingCart> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());

        //对于价格的计算开始------------------------
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetailList = shoppingCartService.list(queryWrapper).stream().map(item -> {
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            //价格计算结束-----------------------
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(IdWorker.getId());
            orderDetail.setName(item.getName());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            orderDetail.setOrderId(order_id);
            return orderDetail;
        }).collect(Collectors.toList());


        //向订单表插入数据（一条）
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if(addressBook == null){
//            throw new MyException("用户地址信息有误，不能下单");  //这里有问题，前端传来的地址id查不到，用默认地址代替
            orders.setAddress("默认地址");
        }else{
            orders.setAddress(addressBook.getDetail());
        }
        orders.setId(order_id); //生成一个订单号
        orders.setStatus(2);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setUserName(user.getName());
        orders.setPhone(user.getPhone());
        orders.setConsignee("ConsigneeDemo");
        orders.setAmount(new BigDecimal(amount.get())); //设置总金额
        this.save(orders); //调用当前类的方法用this


        //向订单明细表插入数据（多条）
        orderDetailService.saveBatch(orderDetailList);
        //清空购物车数据
        shoppingCartService.remove(queryWrapper);
    }
}
