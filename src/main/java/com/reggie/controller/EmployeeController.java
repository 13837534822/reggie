package com.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.po.Employee;
import com.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")  //在路径前面加一个/employee
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String username = employee.getUsername();
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());  //password转为MD5加密
        //mybatis-plus的条件构造器
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        Employee emp = employeeService.getOne(wrapper); //查询一条数据
        //查询不到用户
        if (emp == null) {
            return Result.error("登录失败，返回为null");
        }
        //密码比对
        if (!emp.getPassword().equals(password)) {
            return Result.error("登录失败，密码比对错误");
        }
        //查看用户状态
        if (emp.getStatus() == 0) {
            return Result.error("登陆失败，账号禁用");
        }
        //登录成功
        request.getSession().setAttribute("employee", emp.getId());
        BaseContext.setCurrentId(emp.getId());
        return Result.success(emp);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        //清理session中的登录员工信息
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    @PostMapping
    public Result<String> add(HttpServletRequest request, @RequestBody Employee employee) {  //请求路径为/employee，所以不用加路径
        System.out.println(employee.toString());
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));  //初始密码设置为123456
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));  //设置创建此用户的的用户id
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));  //设置更新此用户的的用户id
        if (employeeService.save(employee)) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }


    @GetMapping("/page")
    public Result<Page> page(Integer page, Integer pageSize, String name) {
        System.out.println(page + " " + pageSize + " " + name);

        //mp中的页面类型
        Page pageInfo = new Page(page, pageSize);
        //mp中的构造器
        QueryWrapper wrapper = new QueryWrapper();
        if (name != null) {
            wrapper.like("name", "name");
        }
        wrapper.orderByDesc("id"); //按id降序查询
        //执行分页查询
        employeeService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @PutMapping         //请求方式为put和另一个employee不同
    public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        System.out.println(employee.getStatus());
        //在更新业务前存入线程数据，拦截器和该业务不一个线程？！！！！！！
        BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateTime(LocalDateTime.now());
        if (employeeService.updateById(employee)) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @GetMapping("/{id}")  //前端请求的地址为http://localhost:8080/employee/1818550669695635458 (最后数字是id)
    public Result<Employee> getEmployeeById(@PathVariable Long id) {
        System.out.println(id);
        Employee employee = employeeService.getById(id);
        if (employee == null) {
            return Result.error("查询用户失败");
        }
        return Result.success(employee);
    }
}
