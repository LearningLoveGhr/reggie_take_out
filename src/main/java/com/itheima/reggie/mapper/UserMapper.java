package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ghr
 * @create 2023-02-01-15:07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
