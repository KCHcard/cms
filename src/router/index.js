import VueRouter from 'vue-router';

const routes =[
    {
        path:'/',
        name:'login',
        component:()=>import('../components/Cms_Login.vue')
    },
    {
        path:'/Index',
        name:'index',
        component:()=>import('../components/IndexPage.vue'),
        children:[
            {
                path:'/Home',
                name:'home',
                meta:{
                    title:'首页'
                },
                component:()=>import('../components/UserHome.vue'),
            },
            /*{
                path:'/Admin',
                name:'admin',
                meta:{
                    title:'管理员管理'
                },
                component:()=>import('../components/admin/AdminManage.vue')
            },
            {
                path:'/User',
                name:'user',
                meta:{
                    title:'店长管理'
                },
                component:()=>import('../components/user/UserManage.vue')
            },*/
        ]
    },
    {
        path: '/',
        name: 'DishManage',
        component:()=>import('../components/dish/DishManage.vue'),
    },
]
const router = new VueRouter({
    mode:'history',
    routes
})

export function resetRouter() {
    router.matcher = new VueRouter({
        mode:'history',
        routes: []
    }).matcher
}
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

router.beforeEach((to, from, next) => {
    document.title = to.meta.title || '餐饮后台管理系统-Cms';
    next();
});

export  default router;