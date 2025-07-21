package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.reggie.common.MailUtils;
import com.reggie.common.Result;
import com.reggie.common.ValidateCodeUtils;
import com.reggie.po.User;
import com.reggie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")  //发送验证码
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (phone == null) {
            return Result.error("验证码为空");
        }
        //生成随机验证码并发送
        String code = ValidateCodeUtils.generateValidateCode(4).toString();//四位验证码
        MailUtils.sendMail(phone, "验证码为：" + code, "瑞吉外卖");
        System.out.println("验证码为：" + code);
        session.setAttribute(phone, code);
        return Result.success("验证码发送成功");
    }


    //前台用户登录
    @PostMapping("/login")    //使用map接收前台传来的数据
    public Result<User> login(@RequestBody Map map, HttpSession session) {
        System.out.println("map:" + map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        if (code != null && session.getAttribute(phone).equals(code)) { //验证码比对
            //判断是否为新用户，新用户直接注册
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("phone", phone);
            User user = userService.getOne(wrapper);
            if (user == null) { //为空就是新用户
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);  //注册
                return Result.success(user);
            } else {  //老用户
                return Result.success(user);
            }
        }
        return Result.error("登录失败");
    }
}
