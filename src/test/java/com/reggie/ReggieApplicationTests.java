package com.reggie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reggie.common.BaseContext;
import com.reggie.mapper.EmployeeMapper;
import com.reggie.po.AddressBook;
import com.reggie.po.Employee;
import com.reggie.service.AddressBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import java.util.List;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private AddressBookService addressBookService;

    @Test
    void contextLoads() {
    }

    @Test
    void MpTest() { //Redis的练习测试
        //获取连接
        Jedis jedis = new Jedis("localhost", 6379);
        //设置值
        jedis.set("address", "demo");
        //关闭连接
        jedis.close();

    }

}
