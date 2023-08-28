<template>
  <div>
    <!-- 搜索栏 -->
    <el-card id="search">
      <el-input
        v-model="serachModel.username"
        placeholder="用户名"
        clearable
      ></el-input>
      <el-input
        v-model="serachModel.phone"
        placeholder="电话"
        clearable
      ></el-input>
      <el-button @click="getUserList" type="primary" round icon="el-icon-search"
        >查询</el-button
      >
      <el-button
        @click="openEditUI(null)"
        type="primary"
        icon="el-icon-plus"
        circle
        id="add"
      ></el-button>
    </el-card>

    <!-- 结果栏 -->
    <el-card>
      <el-table :data="userList" stripe style="width: 100%">
        <el-table-column label="#" width="100">
          <!-- (pageNo-1)*pageSize + index + 1 -->
          <template slot-scope="scope">
            {{
              (serachModel.pageNo - 1) * serachModel.pageSize + scope.$index + 1
            }}
          </template>
        </el-table-column>

        <el-table-column prop="id" label="用户ID" width="100">
        </el-table-column>

        <el-table-column prop="username" label="用户名" width="160">
        </el-table-column>

        <el-table-column prop="phone" label="电话" width="180">
        </el-table-column>

        <el-table-column prop="status" label="用户状态" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == 1">正常</el-tag>
            <el-tag v-else-if="scope.row.status == 0" type="danger"
              >禁用</el-tag
            >
          </template>
        </el-table-column>

        <el-table-column prop="email" label="邮箱"> </el-table-column>

        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button
              @click="openEditUI(scope.row.id)"
              type="primary"
              icon="el-icon-edit"
              size="mini"
            ></el-button>
            <el-button
              @click="deleteUser(scope.row)"
              type="danger"
              icon="el-icon-delete"
              size="mini"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="serachModel.pageNo"
      :page-sizes="[5, 10, 20, 50]"
      :page-size="serachModel.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>

    <!-- 用户信息编辑对话框 -->
    <el-dialog
      @close="clearForm"
      :title="title"
      :visible.sync="dialogFormVisible"
    >
      <el-form :model="userForm" ref="userFormRef" :rules="rules">
        <el-form-item
          label="用户名"
          prop="username"
          :label-width="formLabelWidth"
        >
          <el-input v-model="userForm.username" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item
          label="登录密码"
          v-if="userForm.id == null || userForm.id == undefined"
          prop="password"
          :label-width="formLabelWidth"
        >
          <el-input
            v-model="userForm.password"
            type="password"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="联系电话" :label-width="formLabelWidth">
          <el-input v-model="userForm.phone" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="用户状态" :label-width="formLabelWidth">
          <el-switch
            v-model="userForm.status"
            :active-value="1"
            :inactive-value="0"
          >
          </el-switch>
        </el-form-item>

        <el-form-item label="用户角色" :label-width="formLabelWidth">
          <el-checkbox-group v-model="userForm.roleIdList" style="80%" :max="2">
            <el-checkbox
              v-for="role in roleList"
              :label="role.roleId"
              :key="role.roleId"
              >{{ role.roleDesc }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="邮箱" prop="email" :label-width="formLabelWidth">
          <el-input v-model="userForm.email" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import userApi from "@/api/userManage";
import roleApi from "@/api/roleManage";
export default {
  data() {
    var checkEmail = (rule, value, callback) => {
      //邮箱正则表达式
      var reg =
        /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
      if (!reg.test(value)) {
        return callback(new Error("邮箱格式错误"));
      }
      callback();
    };
    return {
      roleList: [],
      formLabelWidth: "120px",
      userForm: {
        roleIdList: [],
      },
      dialogFormVisible: false,
      total: 0,
      title: "",
      serachModel: {
        pageNo: 1,
        pageSize: 10,
      },
      userList: [],
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 3,
            max: 20,
            message: "长度在 3 到 20 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
        email: [
          { required: true, message: "请输入邮箱", trigger: "blur" },
          { validator: checkEmail, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    getAllRoleList() {
      roleApi.getAllRoleList().then((response) => {
        this.roleList = response.data;
      });
    },
    deleteUser(user) {
      this.$confirm(`您确认删除用户${user.username} ?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        userApi
          .deleteById(user.id)
          .then((response) => {
            this.$message({
              type: "success",
              message: response.message,
            });
            this.getUserList();
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      });
    },
    saveUser() {
      //表单验证
      this.$refs.userFormRef.validate((valid) => {
        if (valid) {
          //提交表单
          userApi.saveUser(this.userForm).then((response) => {
            //成功提示
            this.$message({
              message: response.message,
              type: "success",
            });
            //关闭对话框
            this.dialogFormVisible = false;
            //刷新列表
            this.getUserList();
          });
        } else {
        }
      });
    },
    clearForm() {
      this.userForm = {
        roleIdList: [],
      };
      this.$refs.userFormRef.clearValidate();
    },
    openEditUI(id) {
      if (id == null) {
        this.title = "新增用户";
      } else {
        this.title = "修改用户";
        //根据id查询用户信息
        userApi.getUserById(id).then((response) => {
          this.userForm = response.data;
        });
      }
      this.dialogFormVisible = true;
    },
    handleSizeChange(val) {
      this.serachModel.pageSize = val;
      this.getUserList();
    },
    handleCurrentChange(val) {
      this.serachModel.pageNo = val;
      this.getUserList();
    },
    getUserList() {
      userApi.getUserList(this.serachModel).then((response) => {
        console.log(response);
        this.userList = response.data.rows;
        this.total = response.data.total;
      });
    },
  },
  created() {
    this.getUserList();
    this.getAllRoleList();
  },
};
</script>

<style>
#search .el-input {
  width: 200px;
  margin-right: 10px;
}
#add {
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