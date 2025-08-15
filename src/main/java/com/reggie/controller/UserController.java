package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reggie.common.BaseContext;
import com.reggie.common.MailUtils;
import com.reggie.common.Result;
import com.reggie.common.ValidateCodeUtils;
import com.reggie.po.User;
import com.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")  //发送验证码
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (phone == null) {
            return Result.error("验证码为空");
        }
        //生成随机验证码并发送
        String code = ValidateCodeUtils.generateValidateCode(4).toString();//四位验证码
//        MailUtils.sendMail(phone, "验证码为：" + code, "瑞吉外卖");
        System.out.println("验证码为：" + code);
//        session.setAttribute(phone, code);
        redisTemplate.opsForValue().set(phone, code,30, TimeUnit.SECONDS);
        return Result.success("验证码发送成功");
    }


    //前台用户登录
    @PostMapping("/login")    //使用map接收前台传来的数据
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        System.out.println("map:" + map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//        if (code != null && session.getAttribute(phone).equals(code)) { //验证码比对
        if (code != null && redisTemplate.opsForValue().get(phone).toString().equals(code)) { //验证码比对
            System.out.println("redis:"+redisTemplate.opsForValue().get(phone));
            //判断是否为新用户，新用户直接注册
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("phone", phone);
            User user = userService.getOne(wrapper);
            if (user == null) { //为空就是新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);  //注册
                System.out.println("新用户登录成功，ID：" + user.getId());
                session.setAttribute("user", user.getId());
                BaseContext.setCurrentId(user.getId());
                return Result.success(user);
            } else {
                System.out.println("旧用户登录成功，ID：" + user.getId());
                session.setAttribute("user", user.getId());
                BaseContext.setCurrentId(user.getId());
                return Result.success(user);
            }
        }
        return Result.error("登录失败");
    }

    @PostMapping("/loginout")
    public Result<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return Result.success("退出成功");
    }
}
