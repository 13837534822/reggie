package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.common.MyException;
import com.reggie.mapper.CategoryMapper;
import com.reggie.po.Category;
import com.reggie.po.Dish;
import com.reggie.po.Setmeal;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        QueryWrapper<Dish> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("category_id", id);
        int count1 = dishService.count(wrapper1);
        if (count1 > 0) {
            throw new MyException("当前分类下关联了菜品");  //抛出我的自定义异常
        }
        QueryWrapper<Setmeal> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("category_id", id);
        int count2 = setmealService.count(wrapper2);
        if (count2 > 0) {
            throw new MyException("当前分类下关联了分类");  //抛出我的自定义异常
        }
        //正常删除
        super.removeById(id);
    }
}
