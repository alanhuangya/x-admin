package com.alan.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author alan
 * @since 2023-08-09
 */
@TableName("x_menu")
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    private String component;

    private String path;

    private String redirect;

    private String name;

    private String title;

    private String icon;

    private Integer parentId;

    private String isLeaf;

    private Boolean hidden;

    // 表示当前菜单的子菜单,不是数据库字段,不会映射到数据库
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    // 表示当前菜单的元数据,不是数据库字段,不会映射到数据库
    @TableField(exist = false)
    private Map<String, Object> meta;

    public Map<String, Object> getMeta() {
        meta = new HashMap<>();
        // 将title和icon封装到meta中
        meta.put("title",title);
        meta.put("icon",icon);
        return meta;
    }


}
