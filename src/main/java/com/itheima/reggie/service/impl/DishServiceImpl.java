package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ghr
 * @create 2023-01-30-14:59
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();  //菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        /*log.info("flavors:{}",flavors);
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        log.info("flavors:{}",flavors);*/

        List<DishFlavor> flavors1 = new LinkedList<>();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
            //log.info("flavor:{}",flavor);
            flavors1.add(flavor);
        }
        //log.info("flavors:{}",flavors1);



        //保存菜品口味数据到菜品口味表dish_flavor
        //dishFlavorService.saveBatch(flavors);
        dishFlavorService.saveBatch(flavors1);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //查询菜品基本信息,从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        QueryWrapper<DishFlavor> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dish_id",dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    /**
     * 更新菜品信息，同时更新对应的口味信息
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应的口味信息----dish_flavor表的delete操作
        QueryWrapper<DishFlavor> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("dish_id",dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加当前提交过来的口味数据----dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> flavors1=new LinkedList<>();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            flavors1.add(flavor);
        }
        dishFlavorService.saveBatch(flavors1);
    }
}
