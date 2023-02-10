package com.itheima.reggie.common;

/**
 * @author ghr
 * @create 2023-01-30-11:09
 */

/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
