package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.po.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
