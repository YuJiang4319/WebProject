<template>
  <div class="forgot-password-container">
    <h1>找回密码</h1>
    <form class="forgot-password-form">
      <div class="form-item">
        <label for="email">邮箱：</label>
        <input type="email" id="email" v-model="email">
      </div>
      <div class="form-item">
        <button type="button" class="register-button" @click="reset_sendcode(this.email)">发送验证码</button>
      </div>
      <div>
        <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg4 }}</div>
      </div>
      <div class="form-item">
        <label for="code">验证码：</label>
        <input type="number" id="code" v-model="code">
      </div>
      <div class="form-item">
        <button type="button" class="register-button" @click="reset_authcode(this.email, this.code)">点击验证</button>
      </div>
      <div>
        <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg5 }}</div>
      </div>
      <div class="form-item">
        <label for="password">重置密码：</label>
        <input type="password" id="password" v-model="password">
      </div>
      <div class="form-item">
        <button type="button" class="register-button" @click="reset(this.password,this.ticket)">确认</button>
      </div>
      <div>
        <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg6 }}</div>
      </div>
    </form>
  </div>
</template>
  
<script>
export default {
  name: 'reset',
  data() {
    return {
      email: '',
      code: '',
      password: '',
      ticket: null,
      showElement:false,
      msg4: '',
      msg5: '',
      msg6: '',
    }
  },
  methods: {
    reset_sendcode(email) {
      console.log('点击成功' + email),
      this.$request.post('/help/send-email-code', {
        email: email
      }, {
        headers: {
          'Content-type': 'application/x-www-form-urlencoded',
          'Access-Control-Allow-Origin': '*',
        }
      },
      )
      .then(res => {
        console.log('发送成功'),
        console.log(res)
        if(res.data.code==0){
          this.msg4 = res.data.msg
          this.showElement = true
        }else if(res.data.code==1){
          this.msg4 = '发送成功'
          this.showElement = true
        }
      })
    },
    reset_authcode(email, code) {
      console.log('点击成功' + email + code),
      this.$request.post('/help/auth-email-code', {
        email: email,
        emailCode: code
      }, {
        headers: {
          'Content-type': 'application/x-www-form-urlencoded',
          'Access-Control-Allow-Origin': '*',
        }
      },
      )
      .then(res => {
        console.log('发送成功'),
        console.log(res)
        if(res.data.code == 0){
          this.msg5 = '验证失败'
          this.showElement = true
        }else if(res.data.code == 1){
          this.msg5 = '验证成功'
          this.ticket = res.data.data.ticket
          this.showElement = true
        }
      })
    },
    reset(password,ticket) {
      console.log(ticket)
      this.$request.post('/help/reset-password/'+'?ticket='+this.ticket+'&newPassword='+this.password, {
      }, {
        headers: {
          'Access-Control-Allow-Origin': '*',
        }
      },
      )
      .then(res => {
        console.log('发送成功'),
        console.log(res)
        if(res.data.code==0){
          this.msg6 = res.data.msg
          this.showElement = true
        }else if(res.data.code==1){
          this.msg6 = '重置成功'
          this.showElement = true
        }
      })
    }
  }
}
</script>
  
<style>
.forgot-password-container {
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

.forgot-password-form {
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

.forgot-password-button {
  font-size: 1.6rem;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  border: none;
  background-color: #1abc9c;
  color: #fff;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.forgot-password-button:hover {
  transform: scale(1.1);
}
</style>
  