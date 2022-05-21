import Vue from "vue";
import Vuex from "vuex";
import ws from "@/net/socket";
import { getLocal } from '@/utils/mylocal'
import {Notification} from 'element-ui'



Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    // 用户信息
    user: getLocal('userInfo') || {},
    currentChat: { // 当前窗口信息
      who: '',
      name: ''
    },
    isNeedPush: 0, // 控制了解界面刷新显示消息
  },
  mutations: {
    // 初始化连接 注册
    initConnect (state, callback) {
      const u = {
        event: "reg",
        from: state.user.userId,
        username: state.user.username,
        to: "all",
        data: state.user.username + '上线啦'
      }
      ws.open(u);
      ws.message((msg)=>{
        msg = JSON.parse(msg);
        if (msg.event == 'msg') {
          // 消息
          callback(msg);
        } else if (msg.event == 'reg') {
          Notification.info({
            title: '消息',
            message: msg.data
          });
        }
      });
    },

    // 消息推送
    send(state, conf) {
      let _conf = {
        event: "msg",
        from: state.user.userId,
        username: state.user.username,
        to: "all",
        data: ""
      };
      _conf = Object.assign(_conf, conf);
      ws.send(_conf);
    },

    // 保存聊天记录 sessionStorage.historyChat
    addChatStorage(state, info) {
      let hc = (sessionStorage.historyChat && JSON.parse(sessionStorage.historyChat)) || {};
      const _ct = {
        sendobj: info.sendobj,   // 谁发的
        content: info.content,  // 发送内容
        myHeadUrl: info.myHeadUrl,
        username: info.username, //发送人名称
        headUrl: info.headUrl,
        window: info.window, // 聊天窗口对象
        date: Number(new Date())
      }
      // 聊天记录对应某个聊天窗口对象
      if (!hc[_ct.window]) {
        hc[_ct.window] = [];
      }
      hc[_ct.window].push(_ct);
      sessionStorage.historyChat = JSON.stringify(hc);
    },

    /**
     * 新消息提示
     */
    newMsg (state, cwindow) {
      // 先判断 如果是当前窗口，直接更新 无需加入新消息提示数组
      if (cwindow == state.currentChat.who) {
        state.isNeedPush++;  // 改变即可
      } else {
      //  // 没有则加入提示数组
      //  if (!state.noRead.includes(String(cwindow))) {
      //    state.noRead.push(String(cwindow));
      //  }
      }
   },

    setUserInfo (state, data) {
      Object.assign(state.user,data)
    }
  },
  actions: {},
  modules: {}
});
