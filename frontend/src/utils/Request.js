import axios from 'axios'
import qs from 'qs'

const axiosInstance = axios.create({
  timeout: 10000, // 接口最大请求时间
  baseURL: process.env.VUE_APP_API_URL // 接口url前缀
})

// 请求拦截器
axiosInstance.interceptors.request.use(config => {
  return config
}, () => {
   // 错误提示
})

// 响应拦截器
axiosInstance.interceptors.response.use(response => {
  return response.data // 请求成功，返回响应数据
}, () => {
  // 错误提示
  return false
})

class Request {
  async request (url, params, headers, method = 'POST') {
    method = method.toUpperCase()
    const axiosConfig = {
      url: url,
      method: method,
      headers: headers
    }
    if (method === 'POST') {
      axiosConfig.data = qs.stringify(params)
    } else {
      axiosConfig.params = params
    }
    let result = null
    try {
      result = await axiosInstance(axiosConfig).then(response => {
        if (response) {
          if (response.code === 0 || response.code === '0') {
            return response
          } else {
            return false
          }
        } else {
          return false
        }
      })
    } catch (e) {
      result = false
    }
    return result
  }
}

export default Request
