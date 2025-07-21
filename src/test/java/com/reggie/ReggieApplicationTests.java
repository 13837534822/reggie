package com.reggie;

import com.reggie.mapper.EmployeeMapper;
import com.reggie.po.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReggieApplicationTests {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void MpTest() {
        List<Employee> list = employeeMapper.selectList(null);
        System.out.println(list);
    }

}
