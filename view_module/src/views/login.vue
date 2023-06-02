<template>
    <div class="login-container">
      <h1>欢迎登录</h1>
      <form class="login-form">
        <div class="form-item">
          <label for="email">邮箱：</label>
          <input type="text" id="email" v-model="email">
        </div>
        <div class="form-item">
          <label for="password">密码：</label>
          <input type="password" id="password" v-model="password">
        </div>
        <div class="form-item">
          <button type="button" class="login-button" @click="login(this.email, this.password)">登录</button>
        </div>
        <div>
          <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg }}</div>
        </div>
      </form>
    </div>
</template>
  
<script>
import CryptoJS from 'crypto-js'
export default {
  name: 'login',
  data() {
    return {
      email: '',
      password: '',
      password_md5:'',
      showElement:false,
      msg: ''
    }
  },
  methods: {
    login(email, password,password_md5) {
      console.log('点击成功'+email),
      password_md5 = CryptoJS.MD5(password).toString(),
      console.log(password_md5),
        this.$request.post('/login/from-email',{
                email:email,
                password:password_md5
            },{
                headers: {
                    'Content-type': 'application/x-www-form-urlencoded',
                    'Access-Control-Allow-Origin': '*',
                }
            },
        )
        .then(res => {
            console.log('登录成功'),
            console.log(res)
            if(res.data.code==0){
              this.msg = '登录失败'
              this.showElement = true
            }else if(res.data.code==1){
              this.msg = '登录成功'
              this.showElement = true
            }
        })
    }
  }
}
</script>
  
<style>
.login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
    background-color: #f1f1f1;
}
  
h1 {
    font-size: 3rem;
    margin-bottom: 2rem;
}
  
.login-form {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: #fff;
    padding: 3rem 6rem;
    border-radius: 5px;
    box-shadow: 0px 0px 5px #ccc;
}
  
.form-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    margin-bottom: 2rem;
}
  
label {
    font-size: 1.4rem;
}
  
input {
    font-size: 1.4rem;
    padding: 0.5rem 1rem;
    border-radius: 5px;
    border: none;
    box-shadow: 0px 0px 5px #ccc;
    width: 70%;
}
  
.login-button {
    font-size: 1.6rem;
    padding: 0.5rem 1rem;
    border-radius: 5px;
    border: none;
    background-color: #1abc9c;
    color: #fff;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
}
  
.login-button:hover {
    transform: scale(1.1);
}
</style>
  