package com.itheima.reggie.common;

/**
 * @author ghr
 * @create 2023-01-30-15:11
 * 自定义业务异常
 */
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
