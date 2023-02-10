package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @author ghr
 * @create 2023-01-30-11:25
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
