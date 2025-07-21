package com.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;


//新增和更新时某些公共字段可以自动填充
@Component  //让spring框架来管理他
public class MyMetaObjecthandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) { //执行插入时才会去执行
        System.out.println("插入填充");
        //metaObject.setValue("password", DigestUtils.md5DigestAsHex("123456".getBytes()));
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());  //因为这里不能有Httpservletrequest
        metaObject.setValue("updateUser", BaseContext.getCurrentId());  //所以用多线程来取当前登录用户的ID
    }

    @Override
    public void updateFill(MetaObject metaObject) {  //更新时才会去执行
        System.out.println("更新填充");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
