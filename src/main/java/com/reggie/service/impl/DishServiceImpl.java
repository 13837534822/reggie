package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.DishDto;
import com.reggie.mapper.DishMapper;
import com.reggie.po.Dish;
import com.reggie.po.DishFlavor;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override

    @Transactional  //开启事务注解（因为同时操作两张表，因此需要事务来保证一致性）
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto); //保存菜品表
        //菜品id
        Long dishId = dishDto.getId();
        //保存菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dishId);
        }
        dishFlavorService.saveBatch(flavors);  //保存菜品对应的口味
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("dish_id", id);
        List<DishFlavor> flavors = dishFlavorService.list(wrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);  //不用再把dish中属性一个个个set到dishDto中
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品表
        this.updateById(dishDto);
        //清理当前的口味口味表
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("dish_id", dishDto.getId());
        dishFlavorService.remove(wrapper);
        //添加新的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0; i < flavors.size(); i++) {
            flavors.get(i).setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);
    }
}
