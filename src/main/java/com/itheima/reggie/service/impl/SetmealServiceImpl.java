package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ghr
 * @create 2023-01-30-14:57
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    //新增套餐，同时保存套餐和菜品的关联关系，需要操作两种表：Setmeal,setmealdish
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        //保存套餐的基本信息，操作Setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishes1=new LinkedList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishes1.add(setmealDish);
        }
        //保存套餐和菜品的关联关系，操作Setmeal_dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes1);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {

        QueryWrapper<Setmeal> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("id",ids).eq("status",1);
        //查询套餐的状态，确定是否可以删除
        int count = this.count(queryWrapper);
        if (count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，先删除表中的数据 ----setmeal
        this.removeByIds(ids);
        //删除关系表中的数据 ----setmeal_dish
        QueryWrapper<SetmealDish> queryWrapper1=new QueryWrapper<>();
        queryWrapper.in("setmeal_id",ids);

        setmealDishService.remove(queryWrapper1);
    }
}
