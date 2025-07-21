package com.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.mapper.EmployeeMapper;
import com.reggie.po.Employee;
import com.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
