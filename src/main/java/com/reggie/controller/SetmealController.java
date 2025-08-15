package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.dto.SetmealDto;
import com.reggie.po.Setmeal;
import com.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody SetmealDto setmealDto) {
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        setmealService.saveWithDish(setmealDto);
        return Result.success("保存成功");
    }

    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize) {
        Page pageInfo = new Page(page, pageSize);
        setmealService.page(pageInfo);
        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) { //使用使用集合接收时候要加上@RequestParam
        System.out.println(ids.toString());
        setmealService.deleteWithDish(ids);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<List<Setmeal>> list( Setmeal setmeal) {
        List<Setmeal> list = setmealService.list(new QueryWrapper<Setmeal>()
                .eq("category_id", setmeal.getCategoryId())
                .eq("status", setmeal.getStatus()));
        return Result.success(list);
    }
}
