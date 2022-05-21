// 封装对所有localStorage的操作

// 封装一个存储数据到localStorage中的方法
const setLocal = (key, value) => {
    window.localStorage.setItem(key, JSON.stringify(value))
  }
  // 封装一个从localStorage中获取数据的方法
  const getLocal = (key) => {
    return JSON.parse(window.localStorage.getItem(key))
  }
  // 封装一个从localStorage中 删除数据的方法
  const removeLocal = (key) => {
    window.localStorage.removeItem(key)
  }
  // 导出
  export { setLocal, getLocal, removeLocal }