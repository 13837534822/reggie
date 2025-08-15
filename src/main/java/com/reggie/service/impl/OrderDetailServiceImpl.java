package com.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;
import com.reggie.po.OrderDetail;
import com.reggie.mapper.OrderDetailMapper;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
