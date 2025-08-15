package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.po.Category;
import com.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/category")
@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Category category) {
        System.out.println(category.toString());
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        if (categoryService.save(category)) {
            return Result.success("录入成功");
        }
        return Result.error("录入失败");
    }

    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize) {
        Page pageInfo = new Page(page, pageSize);
        categoryService.page(pageInfo);
        return Result.success(pageInfo);
    }

    @DeleteMapping
    public Result<String> delete(Long id) {
        categoryService.remove(id);   //因为要是删除失败则转为异常处理，异常会返回失败信息，所以这里只用返回成功的结果
        return Result.success("删除成功");
    }

    //根据id修改分类
    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Category category) {
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    @GetMapping("/list")
    public Result<List<Category>> list() {
        QueryWrapper<Category> wrapper = new QueryWrapper();
        List<Category> list = categoryService.list(wrapper);
        return Result.success(list);
    }
}
