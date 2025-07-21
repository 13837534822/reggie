package com.reggie.common;

/*
 * 利用多线程来进行获取和保存登录用户ID
 * 目的
 * 1、多个用户请求可能由不同的线程同时处理。如果使用静态变量或单例共享变量来保存用户信息，会出现线程安全问题（如 A 用户的数据被 B 用户覆盖）。
 * 2、保存线程上下文信息，使用 ThreadLocal 可以将用户 ID 绑定到当前线程，方便在任意地方通过 BaseContext.getCurrentId() 获取
 * */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
        System.out.println("设置线程：" + id);
    }

    public static Long getCurrentId() {
        System.out.println("获取线程：" + threadLocal.get());
        return threadLocal.get();
    }
}
