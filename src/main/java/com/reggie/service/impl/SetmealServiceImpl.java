package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.dto.SetmealDto;
import com.reggie.mapper.SetmealMapper;
import com.reggie.po.Setmeal;
import com.reggie.po.SetmealDish;
import com.reggie.service.SetmealDishService;
import com.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> list = setmealDto.getSetmealDishes();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(list);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        this.removeByIds(ids);//删除套餐表
        QueryWrapper queryWrapper = new QueryWrapper();
        for (int i = 0; i < ids.size(); i++) {
            queryWrapper.eq("setmeal_id", ids.get(i));
        }
        setmealDishService.remove(queryWrapper);
    }
}
