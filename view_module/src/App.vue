<script>
import register from './views/register.vue';
import login from './views/login.vue';
import reset from './views/reset.vue';

export default{
  name:'App',
  components:{
    register,
    login,
    reset
  }
}
</script>

<template>
  <div class="parent">
    <router-link to="/register" style="padding: 0.5rem 1rem;">注册</router-link>     |
  
    <router-link to="/login" style="padding: 0.5rem 1rem;">登录</router-link>       |
    
    <router-link to="/reset" style="padding: 0.5rem 1rem;">重置</router-link>

  </div>
  <Router-view></Router-view>
</template>

<style>
.parent {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3rem;
  /* padding: 0.5rem 1rem; */
  border-radius: 5px;
  border: none;
  background-color: #c7e0f0;
  color: #ffffff;
  cursor: pointer;
  transition: all 0.2s ease-in-out;   
}

</style>
