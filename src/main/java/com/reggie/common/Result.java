package com.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/*
通用返回结果
T、E、K、V、？本质都是通配符。用于定义泛型类、泛型方法、泛型接口…换成其他字母也行，只是这几个字母是一种编码约定。
T，即type，表示一个具体的Java类型
E，即element，常用于集合，如List<E>、Set<E>
K V 即key . value，常用于Map的键值对
* */

@Data
public class Result<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> Result<T> success(T object) {
        Result<T> r = new Result<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result r = new Result();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
