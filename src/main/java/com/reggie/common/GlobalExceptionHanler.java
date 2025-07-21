package com.reggie.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHanler {

    @ExceptionHandler(Exception.class)
    public Result<String> ex(Exception ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();

        return Result.error("失败了，请联系管理员");
    }


    //我自定义异常的全局捕获器
    @ExceptionHandler(MyException.class)
    public Result<String> myExceptionHandler(MyException ex) {
        System.out.println(ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
