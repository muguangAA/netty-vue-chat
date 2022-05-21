<template>
  <div class="homepage-hero-module">
    <div class="video-container">
      <div :style="fixStyle" class="filter"></div>
      <video :style="fixStyle" autoplay loop class="fillWidth" muted='muted' v-on:canplay="canplay">
        <source src="../assets/img/coverr-52-new-zealand-lake-queenstown-mountains-0236.mp4" type="video/mp4"/>
        浏览器不支持 video 标签，建议升级浏览器。
      </video>
      <div class="poster hidden" v-if="!vedioCanPlay">
        <img :style="fixStyle" src="没有播放时的图片路径" alt="">
      </div>
    </div>
    <div class="box">
      <div class="form-container">
        <div class="login-title">请登录</div>
        <!-- 用户名密码输入框 -->
        <input  placeholder="用户名/手机号" v-model="user.username"  class="input-item"/>
        <input  placeholder="密码" v-model="user.password"  class="input-item"/>
        <!-- 登录按钮 -->
        <div class="loginbtn" @click="login()">登录</div>
      </div>
    </div>
  </div>
</template>

<script>
  import { setLocal } from '@/utils/mylocal'
  import User from '@/api/User'
  const UserM = new User()
  export default {
    name: 'login',
    data() {
      return {
        vedioCanPlay: false,
        fixStyle: '',
        user: {
          username: '', // 用户名
          password: '', // 密码
          code: '' // 验证码
        }
      }
    },
    mounted() {
      window.onresize = () => {
        const windowWidth = document.body.clientWidth
        const windowHeight = document.body.clientHeight
        const windowAspectRatio = windowHeight / windowWidth
        let videoWidth
        let videoHeight
        if (windowAspectRatio < 0.5625) {
          videoWidth = windowWidth
          videoHeight = videoWidth * 0.5625
          this.fixStyle = {
            height: windowWidth * 0.5625 + 'px',
            width: windowWidth + 'px',
            'margin-bottom': (windowHeight - videoHeight) / 2 + 'px',
            'margin-left': 'initial'
          }
        } else {
          videoHeight = windowHeight
          videoWidth = videoHeight / 0.5625
          this.fixStyle = {
            height: windowHeight + 'px',
            width: windowHeight / 0.5625 + 'px',
            'margin-left': (windowWidth - videoWidth) / 2 + 'px',
            'margin-bottom': 'initial'
          }
        }
      }
      window.onresize()
    },
    methods: {
      canplay() {
        this.vedioCanPlay = true
      },

      // 点击登录按钮 校验成功之后要做的事情
      login () {
        if(this.user.username=="" || this.user.password==""){
          return false;
        }
        UserM.login(this.user).then(res => {
          if (res) {
            // 2.0登录成功保存token与refresh_token 到vuex跟localstorage中
            this.$store.commit('setUserInfo', res.data)
            setLocal('userInfo', res.data)
            // 3.0登录成功跳转到首页
            this.$router.push('/addressBook')
          }
        })
      }
    },
  }
</script>

<style lang="less" scoped>
  .homepage-hero-module,
  .video-container {
    position: relative;
    height: 100vh;
    overflow: hidden;
  }
 
  .video-container .poster img{
    z-index: 0;
    position: absolute;
  }
 
  .video-container .filter {
    z-index: 1;
    position: absolute;
    background: rgba(0, 0, 0, 0.4);
    width: 100%;
  }
 
  .fillWidth {
    width: 100%;
  }

  .box{
	  position: absolute;
    top: 0px;
	  z-index: 100;
    width: 100%;
    height: 100%;
  }

  .form-container{
    position: relative;
    width:75%;
    max-width: 300px;
    top: 50%;
    left: 50%;
    transform: translate(-50%,-60%);
  }

  input{
    width: 100%;
    background-color: rgba(255,255,255,0.3);
    border: none;
  }

  .login-title{
    line-height: 46px;
    text-align: center;
    color: #fff;
    margin-bottom: 10px;
  }

  .input-item::-webkit-input-placeholder{
    color: #fff;
  } 

  .input-item{
    height: 46px;
    border-radius: 23px;
    margin: 0 0 20px 0px;
    color: #fff;
    line-height: 46px;
    text-align: center;
  }

  .loginbtn{
    height: 46px;
    border-radius: 23px;
    color: #000;
    background-color: #fff;
    line-height: 46px;
    text-align: center;
  }
</style>