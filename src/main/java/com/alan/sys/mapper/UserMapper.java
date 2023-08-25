package com.alan.sys.mapper;

import com.alan.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2023-08-09
 */

public interface UserMapper extends BaseMapper<User> {
    public List<String> getRoleNameByUserId(Integer id);
}
