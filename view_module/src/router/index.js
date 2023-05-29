import { createRouter, createWebHashHistory } from 'vue-router'
import register from "../views/register.vue"
import login from '../views/login.vue'
import reset from '../views/reset.vue'

//配置所需页面相关配置

const routes = [
    {
        path: "/register",
        component: register
    },
    {
        path: "/login",
        component: login
    },
    {
        path: "/reset",
        component: reset
    }
]

const router = createRouter({
    history:createWebHashHistory(),
    routes
})

export default router