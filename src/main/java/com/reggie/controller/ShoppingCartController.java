package com.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.po.ShoppingCart;
import com.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<String> add(@RequestBody ShoppingCart shoppingCart , HttpServletRequest request) {
        //设置用户ID
        Long userId = (Long) request.getSession().getAttribute("user");
        shoppingCart.setUserId(userId);
        //查询当前菜品或套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if (dishId !=null){
            //增加的是菜品
            QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
            wrapper.eq("dish_id",dishId);
            ShoppingCart old_dish= shoppingCartService.getOne(wrapper);
            if(old_dish!=null){
                old_dish.setNumber(old_dish.getNumber()+1);
                shoppingCartService.updateById(old_dish);
            }else{
                shoppingCart.setNumber(1);
                shoppingCartService.save(shoppingCart);
            }

        }else{
            //增加的是套餐
            QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
            wrapper.eq("setmeal_id",setmealId);
            ShoppingCart old_setmeal= shoppingCartService.getOne(wrapper);
            if(old_setmeal!=null){
                old_setmeal.setNumber(old_setmeal.getNumber()+1);
                shoppingCartService.updateById(old_setmeal);
            }else{
                shoppingCart.setNumber(1);
                shoppingCartService.save(shoppingCart);
            }
        }
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(HttpServletRequest request) {
        Long currentId = (Long)request.getSession().getAttribute("user");
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",currentId);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result<String> clean(HttpServletRequest  request) {
        Long currentId = (Long)request.getSession().getAttribute("user");
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",currentId);
        shoppingCartService.remove(wrapper);
        return Result.success("清空购物车成功");
    }

    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart,HttpServletRequest request){
        Long currentId = (Long)request.getSession().getAttribute("user");
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",currentId);
        if (shoppingCart.getDishId()!=null){
            wrapper.eq("dish_id",shoppingCart.getDishId());
        }else{
            wrapper.eq("setmeal_id",shoppingCart.getSetmealId());
        }
        shoppingCartService.remove(wrapper);
        return Result.success("删除成功");
    }



}
