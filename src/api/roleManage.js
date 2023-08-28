import request from '@/utils/request'

export default {
  //获取角色列表
  getRoleList(pageModel) {
    return request({
      url: '/role/list',
      method: 'get',
      params: {
        pageNo: pageModel.pageNo,
        pageSize: pageModel.pageSize,
        roleName: pageModel.roleName,
      }
    })
  },

  //根据id获取角色
  getRoleById(id) {
    return request({
      url: `/role/${id}`,
      method: 'get'
    })
  },

  //修改角色
  updateRole(role) {
    return request({
      url: '/role/update',
      method: 'put',
      data: role
    })
  },

  //删除角色
  deleteById(id) {
    return request({
      url: `/role/${id}`,
      method: 'delete'
    })
  },




  saveRole(role) {
    if (role.roleId == null || role.roleId == undefined) {
      return this.addRole(role);
    } else {
      return this.updateRole(role);
    }
  },

  //添加用户
  addRole(role) {
    return request({
      url: '/role/add',
      method: 'post',
      data: role
    })
  },

  //获取所有角色
  getAllRoleList() {
    return request({
      url: '/role/all',
      method: 'get'
    })
  }
}
