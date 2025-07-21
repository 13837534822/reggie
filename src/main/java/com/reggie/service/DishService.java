package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.po.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品同时保存口味数据
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品及口味
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品和口味信息
    public void updateWithFlavor(DishDto dishDto);
}
