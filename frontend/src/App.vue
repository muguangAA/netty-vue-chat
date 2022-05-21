<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

*{
  margin: 0px;
  padding: 0px;
}

</style>

<script>
export default {
  name: 'App',
  mounted () {
    this.$store.commit('initConnect', this.msgCallback)
  },
  methods : {
    /**
     * 服务器msg消息返回处理回调函数
     */
    msgCallback(msg) {
      // 直接将聊天记录存储
      const ct = {
        sendobj: msg.from,
        content: msg.data,
        myHeadUrl: msg.headUrl,
        window: msg.to == 'all' ? 'all' : msg.from,
        headUrl: msg.myHeadUrl
      };
      this.$store.commit('addChatStorage', ct); 
      // 之后更新新消息提示
      this.$store.commit('newMsg', ct.window);
    }
  }
}
</script>
