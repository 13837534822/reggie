package com.reggie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.po.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
