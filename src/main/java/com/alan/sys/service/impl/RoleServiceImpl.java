package com.alan.sys.service.impl;

import com.alan.sys.entity.Role;
import com.alan.sys.entity.RoleMenu;
import com.alan.sys.mapper.RoleMapper;
import com.alan.sys.mapper.RoleMenuMapper;
import com.alan.sys.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alan
 * @since 2023-08-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    // 注入角色菜单表, 用于写入角色菜单表
    @Resource
    private RoleMenuMapper roleMenuMapper;

    private void insertRoleMenu(Role role) {
        if (role.getMenuIdList() != null) {
            // 遍历菜单id
            for (Integer menuId : role.getMenuIdList()) {
                // 写入角色菜单表
                roleMenuMapper.insert(new RoleMenu(null, role.getRoleId(), menuId));
            }
        }
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        //1.写入角色表
        this.baseMapper.insert(role);

        //2.写入角色菜单表
        insertRoleMenu(role);
    }

    @Override
    public Role getRoleById(Integer id) {
        //1.查询角色表
        Role role = this.baseMapper.selectById(id);

        //2.查询角色菜单表
        List<Integer> menuIdList = roleMenuMapper.getMenuIdListByRoleId(id);

        //3.将菜单id集合放入角色对象
        role.setMenuIdList(menuIdList);
        return role;
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        //1.修改角色表
        this.baseMapper.updateById(role);

        //2.删除原有权限
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, role.getRoleId());
        roleMenuMapper.delete(wrapper);

        //3.写入新权限
        insertRoleMenu(role);
    }

    @Override
    @Transactional
    public void deleteRole(Integer id) {
        //1.删除角色表
        this.baseMapper.deleteById(id);

        //2.删除角色菜单表
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, id);
        roleMenuMapper.delete(wrapper);
    }
}
