package com.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.dto.DishDto;
import com.reggie.po.Dish;
import com.reggie.service.DishFlavorService;
import com.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @GetMapping("/list")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("category_id", categoryId);

        List<Dish> list = dishService.list(wrapper);

        return Result.success(list);
    }
}
