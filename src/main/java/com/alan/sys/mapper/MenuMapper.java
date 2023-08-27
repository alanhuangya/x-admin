package com.alan.sys.mapper;

import com.alan.sys.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author alan
 * @since 2023-08-09
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * SELECT a.*
     * FROM x_menu a,
     * x_role_menu b,
     * x_user_role c
     * WHERE a.menu_id = b.menu_id
     * AND b.role_id = c.role_id
     * AND c.user_id = #{userId}
     * AND a.parent_id = #{pid}
     */
    @Select("SELECT a.*\n" +
            "         FROM x_menu a,\n" +
            "              x_role_menu b,\n" +
            "              x_user_role c\n" +
            "         WHERE a.menu_id = b.menu_id\n" +
            "           AND b.role_id = c.role_id\n" +
            "           AND c.user_id = #{userId}\n" +
            "           AND a.parent_id = #{pid}")
    public List<Menu> getMunuListByUserId(@Param("userId") Integer userId, @Param("pid") Integer pid);
}

