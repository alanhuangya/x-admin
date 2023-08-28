<template>
  <div>
    <!-- 搜索栏 -->
    <el-card>
      <el-input placeholder="角色名" v-model="pageModel.roleName" clearable>
      </el-input>
      <el-button @click="getRoleList" type="primary" round icon="el-icon-search"
        >查询</el-button
      >
      <el-button
        @click="roleEdit(null)"
        id="addButton"
        type="primary"
        icon="el-icon-plus"
        circle
      ></el-button>
    </el-card>

    <!-- 结果栏 -->
    <el-card>
      <el-table :data="roleList" stripe style="width: 100%">
        <el-table-column label="#" width="180">
          <template slot-scope="scope">
            <!--  (pageNo-1)*pageSize + index + 1 -->
            {{ (pageModel.pageNo - 1) * pageModel.pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>

        <el-table-column prop="roleId" label="角色ID" width="180">
        </el-table-column>

        <el-table-column prop="roleName" label="角色名称" width="180">
        </el-table-column>

        <el-table-column prop="roleDesc" label="角色描述"> </el-table-column>

        <el-table-column prop="操作" label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              @click="roleEdit(scope.row.roleId)"
              type="primary"
              icon="el-icon-edit"
              size="mini"
            ></el-button>

            <el-button
              @click="deleteRole(scope.row)"
              type="danger"
              icon="el-icon-delete"
              size="mini"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页栏 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageModel.pageNo"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="pageModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="400"
    >
    </el-pagination>

    <!--  添加角色弹窗 -->
    <el-dialog
      @close="clearForm"
      :title="title"
      :visible.sync="dialogFormVisible"
    >
      <el-form :model="roleForm" ref="roleFormRef" :rules="rules">
        <el-form-item
          label="角色名称"
          prop="roleName"
          :label-width="formLabelWidth"
        >
          <el-input v-model="roleForm.roleName" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item
          label="角色描述"
          prop="roleDesc"
          :label-width="formLabelWidth"
        >
          <el-input v-model="roleForm.roleDesc" autocomplete="off"></el-input>
        </el-form-item>

        <!-- 树形组件 -->
        <el-form-item
          label="权限设置"
          prop="menuIdList"
          :label-width="formLabelWidth"
        >
          <el-tree
            ref="menuRef"
            :data="menuList"
            :props="menuProps"
            node-key="menuId"
            show-checkbox
            style="width: 80% "
            default-expand-all
          >
          </el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRole">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import roleApi from "@/api/roleManage";
import menuApi from "@/api/menuManage";
export default {
  data() {
    return {
      menuList: [],
      menuProps: {
        children: "children",
        label: "title",
      },
      roleForm: {},
      formLabelWidth: "120px",
      roleList: [],
      title: "",
      dialogFormVisible: false,
      pageModel: {
        pageNo: 1,
        pageSize: 10,
        total: 0,
      },
      rules: {
        roleName: [
          { required: true, message: "请输入角色名", trigger: "blur" },
          {
            min: 1,
            max: 20,
            message: "长度在 1 到 20 个字符",
            trigger: "blur",
          },
        ],
        roleDesc: [
          { required: true, message: "请输入角色描述", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    getAllMenu() {
      menuApi.getAllMenu().then((response) => {
        this.menuList = response.data;
      });
    },
    deleteRole(role) {
      this.$confirm(`您确认删除角色${role.roleNamel} ?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        roleApi
          .deleteById(role.roleId)
          .then((response) => {
            this.$message({
              type: "success",
              message: response.message,
            });
            this.getRoleList();
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      });
    },
    clearForm() {
      this.roleForm = {};
      this.$refs.roleFormRef.clearValidate();
      this.$refs.menuRef.setCheckedKeys([]);
    },
    roleEdit(id) {
      if (id == null) {
        this.title = "新增角色";
      } else {
        this.title = "修改角色";
        //根据id查询角色信息
        roleApi.getRoleById(id).then((response) => {
          this.roleForm = response.data;
          this.$refs.menuRef.setCheckedKeys(response.data.menuIdList);
        });
      }
      this.dialogFormVisible = true;
    },
    saveRole() {
      //验证表单
      this.$refs.roleFormRef.validate((validate) => {
        if (validate) {
          //获取选中的菜单
          let halfCheckKeys = this.$refs.menuRef.getHalfCheckedKeys();
          let checkKeys = this.$refs.menuRef.getCheckedKeys();
          this.roleForm.menuIdList = halfCheckKeys.concat(checkKeys);
          //保存角色
          roleApi.saveRole(this.roleForm).then((response) => {
            //成功提示
            this.$message({
              type: "success",
              message: response.message,
            });
            //关闭弹窗
            this.dialogFormVisible = false;
            //刷新列表
            this.getRoleList();
          });
        } else {
          return false;
        }
      });
    },
    handleSizeChange(val) {
      this.pageModel.pageSize = val;
      this.getRoleList();
    },
    handleCurrentChange(val) {
      this.pageModel.pageNo = val;
      this.getRoleList();
    },

    // 查询
    getRoleList() {
      roleApi.getRoleList(this.pageModel).then((response) => {
        console.log(response.data);
        this.roleList = response.data.rows;
        this.pageModel.total = response.data.total;
      });
    },
  },
  created() {
    this.getRoleList();
    this.getAllMenu();
  },
};
</script>

<style>
.el-card {
  margin-bottom: 20px;
}
.el-card .el-input {
  width: 200px;
  margin-right: 20px;
}
#addButton {
  float: right;
}

.el-dialog {
  width: 30%;
}
.el-dialog .el-input {
  width: 80%;
  margin-right: 10px;
}
</style>