package com.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.dto.DishDto;
import com.reggie.po.Dish;
import com.reggie.po.DishFlavor;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody DishDto dishDto) {
        System.out.println(dishDto.toString());
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        dishService.saveWithFlavor(dishDto);
        return Result.success("菜品添加成功");
    }

    @GetMapping("page")
    public Result<Page<Dish>> page(int page, int pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        if (name != null) {
            wrapper.like("name", name);
        }
        wrapper.orderByDesc("id");
        dishService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @GetMapping("/{id}")
    public Result<DishDto> getDishById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        if (dishDto == null) {
            return Result.error("菜品查询失败");
        }
        return Result.success(dishDto);
    }

    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody DishDto dishDto) {
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        dishService.updateWithFlavor(dishDto);
        return Result.success("修改成功");
    }

//    @GetMapping("/list")
//    public Result<List<Dish>> getDishByCategoryId(Long categoryId) {
//        QueryWrapper wrapper = new QueryWrapper();
//        wrapper.eq("category_id", categoryId);
//
//        List<Dish> list = dishService.list(wrapper);
//
//        return Result.success(list);
//    }
    @GetMapping("/list")
    public Result<List<DishDto>> getDishByCategoryId(Long categoryId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("category_id", categoryId);

        List<Dish> list = dishService.list(wrapper);
        //利用stream流来处理结果
        List<DishDto> result = list.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            //!!!将dish对象的属性复制到dishDto对象中。这通常用于对象之间的属性复制。根据属性值来对应，没有的属性赋值null
            BeanUtils.copyProperties(dish, dishDto);
            List<DishFlavor> flavors = dishFlavorService.list(new QueryWrapper<DishFlavor>().eq("dish_id", dish.getId()));
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return Result.success(result);

        //优化：先从redis中查询数据，如果存在，直接返回，不存在，再查询数据库，将查询到的数据放入redis中。
        //同时，sava和update也要添加清理缓存的操作，保证redis和数据库数据一致性
    }
}
