package com.alan.sys.controller;

import com.alan.common.vo.Result;
import com.alan.sys.entity.Role;
import com.alan.sys.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author alan
 * @since 2023-08-09
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/all")
    public Result<List<Role>> getAllRole() {
        List<Role> roles = roleService.list();
        return Result.success(roles,"查询所有角色成功");
    }

    @GetMapping("/list")
    public Result<Map<String,Object>> getRoleList(@RequestParam(value = "roleName",required = false) String roleName,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();

        // 1.判断roleName是否为空，如果不为空，就拼接条件
        wrapper.eq(StringUtils.hasLength(roleName),Role::getRoleName,roleName);

        // 2.分页查询
        Page<Role> page = new Page<>(pageNo, pageSize);
        roleService.page(page,wrapper);

        // 3.返回结果
        Map<String,Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("rows",page.getRecords());

        return Result.success(data);
    }

    @PostMapping("/add")
    public Result<Map<String,Object>> addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success(null,"添加角色成功");
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable("id") Integer id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    //修改
    @PutMapping("/update")
    public Result<?> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success("修改角色成功");
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> deleteRole(@PathVariable("id") Integer id) {
        roleService.deleteRole(id);
        return Result.success("删除角色成功");
    }

}
