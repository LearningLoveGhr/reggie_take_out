package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * @author ghr
 * @create 2023-02-05-16:29
 */
public interface OrdersService  extends IService<Orders> {

    public void sumbit(Orders orders);
}
