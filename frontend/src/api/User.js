// 用来处理所有与用户相关的网络请求

// 导入自己封装好的axios实例
import Request from '@/utils/Request'

class User extends Request {
  // 1.登录的接口
  login (obj) {
    const url = 'user/login';
    return this.request(url, obj, {}, 'GET');
  }

  addressBook (obj) {
    const url = "user/list";
    return this.request(url, obj, {}, 'GET');
  }
}
export default User
