<template>
  <div class="chat-area">
    <van-nav-bar
      :title="who.username"
      left-text="返回"
      left-arrow
      @click-left="back"
    />
    
    <div class="chat-content" ref="chatbox">
      <!-- recordContent 聊天记录数组-->
      <div v-for="(itemc,indexc) in recordContent" :key="indexc">
        <!-- 对方 -->
        <div class="word" v-if="itemc.sendobj != 'me'">
            <img :src="itemc.head_url">
            <div class="info">
                <div class="info-content">{{itemc.content}}</div>
            </div>
        </div>
        <!-- 我的 -->
        <div class="word-my" v-else>
            <div class="info">
                <div class="info-content">{{itemc.content}}</div>
            </div>
            <img :src="itemc.myHead_url">
        </div>
      </div>
    </div>
    <div class="send-area">
      <input v-model="user_msg" @keyup.enter="send()"/>
      <span @click="send()">发送</span>
    </div>
  </div>
</template>

<script>
import ws from "@/net/socket";
import { Toast } from 'vant';
export default {
  name: "ChatArea",
  data() {
    return {
      recordContent:[],
      user_msg: "",
      who: JSON.parse(this.$route.query.user)
    };
  },
  // 监听器 用于监听何时该刷新数据
  watch: {
    // 改变聊天对象
    '$store.state.currentChat.who': {
      handler(curVal, oldVal) {
        this._resetCurrentChatStorage();
      }
    },
    // 控制刷新 新消息时
    '$store.state.isNeedPush': {
      handler(curVal, oldVal) {
        this._resetCurrentChatStorage();
      }
    }
  },
  computed: {},
  // 页面初始化
  mounted () {
    this._resetCurrentChatStorage();
  },
  created () {
    this.$store.state.currentChat.who = this.who.userId
    this.$store.state.currentChat.name = '单聊'
  },
  methods: {
    back() {
      this.$router.go(-1); //返回上一页
    },
    send() {
      let content = this.user_msg;
      content = content.replace(/^\s*/g,"").replace(/\s*$/g, "").substr(0, 200); // max 最大发送200
      if (!content) {
        return;
      }
      const conf = {
        to: this.who.userId, // 发送给当前窗口聊天对象
        data: content,
        myHeadUrl: this.$store.state.user.headimg,
        headUrl: this.who.headimg
      };
      this.$store.commit("send", conf);
      this.$store.commit('addChatStorage', {  // 保存聊天记录 sessionStorage.historyChat
        content,
        sendobj: 'me', //代表自己发的
        myHeadUrl: this.$store.state.user.headimg,
        username: this.$store.state.user.username, //发送人名称
        window: this.who.userId, // 聊天窗口对象
        headUrl: this.who.headimg
      });
      // 重新初始化 再清空输入框
      this._resetCurrentChatStorage();
      this.user_msg = ""
    },
    /**
     * 重新初始化聊天界面 获取聊天记录
     */
    _resetCurrentChatStorage () {
      const hc = (sessionStorage.historyChat && JSON.parse(sessionStorage.historyChat)) || {};
      let recordContent = hc[this.who.userId] ? hc[this.who.userId] : [];
      for(let i=0;i<recordContent.length;i++){
        if(recordContent[i].myHeadUrl == "default1"){
          recordContent[i].myHead_url = require("../assets/img/default1.jpg")
        }else{
          recordContent[i].myHead_url = require("../assets/img/default2.jpg")
        }

        if(recordContent[i].headUrl == "default1"){
          recordContent[i].head_url = require("../assets/img/default1.jpg")
        }else{
          recordContent[i].head_url = require("../assets/img/default2.jpg")
        }
      }
      this.recordContent = recordContent
      // 同时将滚动条放置聊天底部 这里要延时才行，原因不明
      setTimeout(()=> {
        this.$refs.chatbox.scrollTop = this.$refs.chatbox.scrollHeight;
      }, 10);
    }
  }
};
</script>

<style lang="less" scoped>
.chat-area{
    width: 50%;
    height: 50%;
    margin: auto;
}
.chat-content{
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
  background-color: rgba(110,110,110,0.1);
  height: calc(100% - 86px);
  overflow: scroll;
  .word{
     display: flex;
     margin-bottom: 20px;
     img{
       width: 40px;
       height: 40px;
       border-radius: 50%;
     }
     .info{
       margin-left: 10px;
       .info-content{
          padding: 10px;
          font-size: 14px;
          background: #fff;
          position: relative;
          border-radius: 2px;
        }
        //小三角形
        .info-content::before{
            position: absolute;
            left: -5px;
            top: 10px;
            content: '';
            border-right: 10px solid #FFF;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
        }
     }
  }
 
.word-my{
    display: flex;
    justify-content:flex-end;
    margin-bottom: 20px;
    img{
       width: 40px;
       height: 40px;
       border-radius: 50%;
     }
     .info{
       width: 90%;
       margin-left: 10px;
       text-align: right;
       .info-content{
          max-width: 70%;
          padding: 10px;
          font-size: 14px;
          float: right;
          margin-right: 10px;
          position: relative;
          background: #A3C3F6;
          text-align: left;
          border-radius: 2px;
        }
        //小三角形
        .info-content::after{
            position: absolute;
            right: -5px;
            top: 10px;
            content: '';
            border-left: 10px solid #A3C3F6;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
        }
     }
  }
}
.send-area{
  height: 40px;
  background-color: whitesmoke;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  input{
    flex: 1;
    border: none;
    line-height: 30px;
    text-indent: 10px;
  }
  span{
    height: 30px;
    line-height: 30px;
    padding: 0px 15px;
    border-radius: 2px;
    color: white;
    background-color: #1989fa;
    margin-left: 10px;
  }
}
</style>
