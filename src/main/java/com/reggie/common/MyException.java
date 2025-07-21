package com.reggie.common;

/*
 * 我的自定义异常，本来没有异常的，我抛出一个异常
 *
 * Exception，受检查异常。可以理解为错误，必须要开发者解决以后才能编译通过
 *
 * RunTimeException：运行时异常，又称不受检查异常，不受检查！
 * 不受检查！！不受检查！！！重要的事情说三遍，因为不受检查，
 * 所以在代码中可能会有RunTimeException时Java编译检查时不会告诉你有这个异常，
 * 但是在实际运行代码时则会暴露出来，比如经典的1/0，空指针等。如果不处理也会被Java自己处理。
 * */
public class MyException extends RuntimeException {

    public MyException(String msg) {
        super(msg);
    }
}
