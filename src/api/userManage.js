import request from '@/utils/request'

export default {
  // 获取用户列表
  getUserList(searchModel) {
    return request({
      url: '/user/list',
      method: 'get',
      params: {
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        phone: searchModel.phone,
      }
    })
  },

  // 添加用户
  addUser(user) {
    return request({
      url: '/user/add',
      method: 'post',
      data: user
    })
  },

  // 查询用户
  getUserById(id) {
    return request({
      url: `/user/${id}`, 
      method: 'get'
    })
  },

  // 修改用户
  updateUser(user) {
    return request({
      url: '/user/update',
      method: 'put',
      data: user
    })
  },

  saveUser(user) {
    if(user.id == null || user.id == undefined) {
      return this.addUser(user)
    }
    return this.updateUser(user)
  },

  deleteById(id) {
    return request({
      url: `/user/${id}`,
      method: 'delete'
    });
  }
}
