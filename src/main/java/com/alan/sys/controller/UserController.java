package com.alan.sys.controller;

import com.alan.common.vo.Result;
import com.alan.sys.entity.User;
import com.alan.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author alan
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public Result<List<User>> getAllUser() {
        List<User> list = userService.list();
        return Result.success(list,"查询成功");
    }

    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody User user) {
        Map<String,Object> data = userService.login(user);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20002,"用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token) {
        Map<String, Object> data = userService.getUserInfo(token);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20003,"获取用户信息失败");
    }

    //注销
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success();
    }

    //查询用户
    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,
                                              @RequestParam(value = "phone",required = false) String phone,
                                              @RequestParam(value = "pageNo") Long pageNo,
                                              @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasLength(username),User::getUsername, username);
        queryWrapper.eq(StringUtils.hasLength(phone),User::getPhone, phone);

        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);
    }

    //添加用户
    @PostMapping("/add")
    public Result<?> addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return Result.success("添加成功");
    }

    //修改用户
    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody User user) {
        user.setPassword(null);
        userService.updateById(user);
        return Result.success("修改成功");
    }

    //通过id查询用户
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    //通过id删除用户
    @DeleteMapping("/{id}")
    public Result<?> deleteUserById(@PathVariable("id") Integer id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }
}
