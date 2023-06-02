<template>
    <div class="register-container">
        <h1>欢迎注册</h1>
        <form class="register-form">
            <div class="form-item">
                <label for="email">邮箱：</label>
                <input type="text" id="email" v-model="email">
            </div>
            <div class="form-item">
                <button type="button" class="register-button" @click="register_sendcode(this.email)">发送验证码</button>
            </div>
            <div>
                <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg1 }}</div>
            </div>
            <div class="form-item">
                <label for="code">验证码：</label>
                <input type="number" id="code" v-model="code">
            </div>
            <div class="form-item">
                <button type="button" class="register-button" @click="register_authcode(this.email,this.code)">点击验证</button>
            </div>
            <div>
                <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg2 }}</div>
            </div>
            <div class="form-item">
                <label for="password">密码：</label>
                <input type="password" id="password" v-model="password">
            </div>
            <div class="form-item">
                <button type="button" class="register-button" @click="register(this.password,this.ticket)">注册</button>
            </div>
            <div>
                <div v-show="showElement" style="color: red;font-size:1.5rem ;">{{ msg3 }}</div>
            </div>
        </form>
    </div>
</template>
  
<script>
import CryptoJS from 'crypto-js'
export default {
    data() {
        return {
            email: '',
            password: '',
            password_md5:'',
            code: '',
            ticket: null,
            showElement:false,
            msg1: '',
            msg2: '',
            msg3: '',
        }
    },
    methods: {
        register_sendcode(email) {
            this.$request.post('/register/send-email-code',{
                    email:email
                },{
                    headers: {
                        'Content-type': 'application/x-www-form-urlencoded',
                        'Access-Control-Allow-Origin': '*',
                    }
                },
            )
            .then(res => {
                console.log('验证码发送成功'),
                console.log(res)
                if(res.data.code==0){
                    this.msg1 = res.data.msg
                    this.showElement = true
                }else if(res.data.code==1){
                    this.msg1 = '发送成功'
                    this.showElement = true
                }
            })
        },
        register_authcode(email,code) { 
            this.$request.post('/register/auth-email-code',{
                    email:email,
                    emailCode:code
                },{
                    headers: {
                        'Content-type': 'application/x-www-form-urlencoded',
                        'Access-Control-Allow-Origin': '*',
                    }
                },
            )
            .then(res => {
                console.log('验证码验证成功'),
                console.log(res)
                if(res.data.code==0){
                    this.msg2 = '验证失败'
                    this.showElement = true
                }else if(res.data.code==1){
                    this.msg2 = '验证成功'
                    this.ticket = res.data.data.ticket
                    this.showElement = true
                }
            })
        },
        register(password, ticket,password_md5) { 
            console.log(ticket),
            password_md5 = CryptoJS.MD5(password).toString(),
            console.log(password_md5),
            this.$request.post('/register/new-account/'+'?ticket='+this.ticket,{
                    password:password_md5,
                    // ticket:ticket 
                },{
                    headers: {
                        'Access-Control-Allow-Origin': '*',
                    }
                },
            )
            .then(res => {
                console.log('注册成功'),
                console.log(res)
                if(res.data.code==0){
                    this.msg3 = res.data.msg
                    this.showElement = true
                }else if(res.data.code==1){
                    this.msg3 = '注册成功'
                    this.showElement = true
                }
            })
        }
    }
}
</script>
  
<style>
.register-container {
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
  
.register-form {
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
  
.register-button {
    font-size: 1.6rem;
    padding: 0.5rem 1rem;
    border-radius: 5px;
    border: none;
    background-color: #1abc9c;
    color: #fff;
    cursor: pointer;
    transition: all 0.2s ease-in-out;
}
  
.register-button:hover {
    transform: scale(1.1);
}
</style>
  