package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.mapper.AddressBookMapper;
import com.reggie.po.AddressBook;
import com.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

//@Slf4j
//@RequestMapping("/addressBook")
//@RestController
//public class AddressBookController {
//
//    @Autowired
//    private AddressBookService addressBookService;
//    @Autowired
//    private AddressBookMapper addressBookMapper;
//
//    @PostMapping
//    public R<AddressBook> save(@RequestBody AddressBook addressBook, HttpSession session){
//        Long userId = (Long) session.getAttribute("user");
//        addressBook.setUserId(userId);
//        addressBook.setCreateUser(userId);
//        addressBook.setUpdateUser(userId);
//        System.out.println("addressBook:"+addressBook.toString());
//        addressBookService.save(addressBook);
//        return R.ok(addressBook);
//    }
//
//    @GetMapping("/list")
//    public R<List<AddressBook>> list(HttpSession session) {
//        Long userId = (Long)session.getAttribute("user");
//        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq( "user_id", userId);
//        List<AddressBook> list = addressBookService.list(queryWrapper);
//        return R.ok(list);
//    }
//
//    @PutMapping("default")
//    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook, HttpSession session) {
//        Long userId = (Long)session.getAttribute("user");
//        UpdateWrapper<AddressBook> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("user_id", userId).set("is_default", 0);
//        addressBookService.update(updateWrapper);
//        addressBook.setIsDefault(1);
//        addressBookService.updateById(addressBook);
//        return R.ok(addressBook);
//    }
//    @GetMapping("/{id}")
//    public R get(@PathVariable Long id){
//        AddressBook addressBook = addressBookService.getById(id);
//        if(addressBook == null){
//            return R.failed("查询地址失败");
//        }else{
//            return R.ok(addressBook);
//        }
//
//    }
//}

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        addressBook.setUserId((Long) request.getSession().getAttribute("user"));
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, (Long) request.getSession().getAttribute("user"));
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public Result<AddressBook> getDefault(HttpServletRequest  request) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, (Long) request.getSession().getAttribute("user"));
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return Result.error("没有找到该对象");
        } else {
            return Result.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook, HttpServletRequest request) {
        addressBook.setUserId((Long) request.getSession().getAttribute("user"));
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return Result.success(addressBookService.list(queryWrapper));
    }
}

