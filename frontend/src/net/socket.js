const defaultData = {
  event: 'msg',
  username: '',
  from: '',
  to: "all",
  type: 'text',
  data: ''
}

class WSocket {
  wss;

  constructor() {
    if (!("WebSocket" in window)) {
      alert("您的浏览器不支持 WebSocket!");
    }
  }

  open (c) {
    try {
      this.wss = new WebSocket("ws://127.0.0.1:8888/ws");
    } catch( err ) {
      alert('连接服务器失败');
    }
    c = Object.assign(defaultData, c);
    let _self = this;
    this.wss.onopen = function() {
      this.send(JSON.stringify(c));
      setInterval(function(){
        let c = {
          event: 'heartbeat',
          username: '',
          from: '1015',
          to: "1015",
          type: 'text',
          data: '心跳'
        }
        console.log("心跳")
        _self.send(c)
      }, 50000);
    };
    this.wss.onclose = function (e) {
      console.log('websocket 断开: ' + e.code + ' ' + e.reason + ' ' + e.wasClean)
      console.log(e)
    }
  }

  message (f) {
    this.wss.onmessage = function(evt) {
      f(evt.data);
    };
  }
  
  send (c) {
    c = Object.assign(defaultData, c);
    this.wss.send(JSON.stringify(c));
  }

}

export default new WSocket();